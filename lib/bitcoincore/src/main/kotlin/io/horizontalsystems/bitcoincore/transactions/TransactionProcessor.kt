package io.horizontalsystems.bitcoincore.transactions

import io.horizontalsystems.bitcoincore.WatchedTransactionManager
import io.horizontalsystems.bitcoincore.blocks.IBlockchainDataListener
import io.horizontalsystems.bitcoincore.core.IStorage
import io.horizontalsystems.bitcoincore.core.ITransactionInfoConverter
import io.horizontalsystems.bitcoincore.core.inTopologicalOrder
import io.horizontalsystems.bitcoincore.extensions.toHexString
import io.horizontalsystems.bitcoincore.extensions.toReversedHex
import io.horizontalsystems.bitcoincore.managers.*
import io.horizontalsystems.bitcoincore.models.Block
import io.horizontalsystems.bitcoincore.models.InvalidTransaction
import io.horizontalsystems.bitcoincore.models.Transaction
import io.horizontalsystems.bitcoincore.storage.FullTransaction
import io.horizontalsystems.bitcoincore.storage.FullTransactionInfo
import java.util.*
import kotlin.collections.HashSet

class TransactionProcessor(
        private val storage: IStorage,
        private val extractor: TransactionExtractor,
        private val outputsCache: OutputsCache,
        private val publicKeyManager: PublicKeyManager,
        private val irregularOutputFinder: IIrregularOutputFinder,
        private val dataListener: IBlockchainDataListener,
        private val txInfoConverter: ITransactionInfoConverter,
        private val transactionMediator: TransactionMediator) {

    private val processedNotMineTransactions = HashSet<NotMineTransaction>()

    var listener: WatchedTransactionManager? = null

    fun processOutgoing(transaction: FullTransaction) {
        if (storage.getTransaction(transaction.header.hash) != null) {
            throw TransactionCreator.TransactionAlreadyExists("hash = ${transaction.header.hash.toReversedHex()}")
        }

        process(transaction)

        storage.addTransaction(transaction)
        dataListener.onTransactionsUpdate(listOf(transaction.header), listOf(), null)

        if (irregularOutputFinder.hasIrregularOutput(transaction.outputs)) {
            throw BloomFilterManager.BloomFilterExpired
        }
    }

    @Throws(BloomFilterManager.BloomFilterExpired::class)
    fun processIncoming(transactions: List<FullTransaction>, block: Block?, skipCheckBloomFilter: Boolean) {
        var needToUpdateBloomFilter = false

        val inserted = mutableListOf<Transaction>()
        val updated = mutableListOf<Transaction>()

        val pendingExists = block == null || storage.incomingPendingTransactionsExist()

        // when the same transaction came in merkle block and from another peer's mempool we need to process it serial
        synchronized(this) {
            for ((index, transaction) in transactions.inTopologicalOrder().withIndex()) {
                val notMineTransaction = NotMineTransaction(transaction.header.hash, block != null)
                if (processedNotMineTransactions.contains(notMineTransaction)) {
                    continue
                }

                if (storage.getInvalidTransaction(transaction.header.hash) != null) {
                    continue
                }

                val transactionInDB = storage.getTransaction(transaction.header.hash)
                if (transactionInDB != null) {

                    if (transactionInDB.blockHash != null || (block == null && transactionInDB.status == Transaction.Status.RELAYED)) {  // if transaction already in block or transaction comes again from memPool we no need to update it
                        continue
                    }
                    relay(transactionInDB, index, block)

                    if (transactionInDB.blockHash != null) {
                        transactionInDB.conflictingTxHash = null
                    }

                    storage.updateTransaction(transactionInDB)

                    updated.add(transactionInDB)
                    continue
                }

                process(transaction)

                listener?.onTransactionReceived(transaction)

                if (transaction.header.isMine) {
                    relay(transaction.header, index, block)

                    val conflictingTransactions = storage.getConflictingTransactions(transaction)
                    val updatedTransactions = mutableListOf<Transaction>()

                    when (transactionMediator.resolveConflicts(transaction, conflictingTransactions, updatedTransactions)) {
                        ConflictResolution.IGNORE -> {
                            updatedTransactions.forEach { storage.updateTransaction(it) }
                            updated.addAll(updatedTransactions)
                        }
                        ConflictResolution.ACCEPT -> {
                            updatedTransactions.forEach { processInvalid(it.hash, transaction.header.hash) }
                            storage.addTransaction(transaction)
                            inserted.add(transaction.header)
                        }
                    }


                    if (!skipCheckBloomFilter) {
                        val checkDoubleSpend = !transaction.header.isOutgoing && block == null
                        needToUpdateBloomFilter = needToUpdateBloomFilter ||
                                checkDoubleSpend ||
                                publicKeyManager.gapShifts() ||
                                irregularOutputFinder.hasIrregularOutput(transaction.outputs)
                    }
                } else if (pendingExists) {

                    processedNotMineTransactions.add(notMineTransaction)

                    val incomingPendingTxHashes = storage.getIncomingPendingTxHashes()

                    if (incomingPendingTxHashes.isEmpty()) {
                        continue
                    }

                    val conflictingTxHashes = storage.getTransactionInputs(incomingPendingTxHashes)
                            .filter { input ->
                                transaction.inputs.any {
                                    it.previousOutputTxHash.contentEquals(input.previousOutputTxHash) && it.previousOutputIndex == input.previousOutputIndex
                                }
                            }
                            .map { it.transactionHash }.distinctBy { it.toHexString() }

                    // handle if transaction has conflicting inputs, otherwise it's false-positive tx
                    if (conflictingTxHashes.isEmpty()) {
                        continue
                    }

                    conflictingTxHashes
                            .mapNotNull { storage.getTransaction(it) } // get transactions for each input
                            .filter { it.blockHash == null } // exclude all transactions in blocks
                            .forEach { tx ->
                                if (block == null) { // if coming other tx is pending only update conflict status
                                    tx.conflictingTxHash = transaction.header.hash
                                    storage.updateTransaction(tx)
                                    updated.add(tx)
                                } else { // if coming other tx in block invalidate our tx
                                    processInvalid(tx.hash, transaction.header.hash)
                                    needToUpdateBloomFilter = true
                                }
                            }
                }
            }
        }

        if (inserted.isNotEmpty() || updated.isNotEmpty()) {
            dataListener.onTransactionsUpdate(inserted, updated, block)
        }

        if (needToUpdateBloomFilter) {
            throw BloomFilterManager.BloomFilterExpired
        }
    }

    fun processInvalid(txHash: ByteArray, conflictingTxHash: ByteArray? = null) {
        val invalidTransactionsFullInfo = getDescendantTransactionsFullInfo(txHash)

        if (invalidTransactionsFullInfo.isEmpty())
            return

        invalidTransactionsFullInfo.forEach { fullTxInfo ->
            conflictingTxHash?.let { conflictingTxHash ->
                fullTxInfo.header.conflictingTxHash = conflictingTxHash
            }
            fullTxInfo.header.status = Transaction.Status.INVALID
        }

        val invalidTransactions = invalidTransactionsFullInfo.map { fullTxInfo ->
            val txInfo = txInfoConverter.transactionInfo(fullTxInfo)
            val serializedTxInfo = txInfo.serialize()
            InvalidTransaction(fullTxInfo.header, serializedTxInfo, fullTxInfo.rawTransaction)
        }

        storage.moveTransactionToInvalidTransactions(invalidTransactions)

        dataListener.onTransactionsUpdate(updated = invalidTransactions, inserted = listOf(), block = null)
    }

    private fun getDescendantTransactionsFullInfo(txHash: ByteArray): List<FullTransactionInfo> {
        val fullTransactionInfo = storage.getFullTransactionInfo(txHash) ?: return listOf()
        val list = mutableListOf(fullTransactionInfo)

        val inputs = storage.getTransactionInputsByPrevOutputTxHash(fullTransactionInfo.header.hash)

        inputs.forEach { input ->
            val descendantTxs = getDescendantTransactionsFullInfo(input.transactionHash)
            list.addAll(descendantTxs)
        }

        return list
    }

    private fun process(transaction: FullTransaction) {
        extractor.extractOutputs(transaction)

        if (outputsCache.hasOutputs(transaction.inputs)) {
            transaction.header.isMine = true
            transaction.header.isOutgoing = true
        }

        if (transaction.header.isMine) {
            outputsCache.add(transaction.outputs)
            extractor.extractAddress(transaction)
            extractor.extractInputs(transaction)
        }
    }

    private fun relay(transaction: Transaction, order: Int, block: Block?) {
        transaction.status = Transaction.Status.RELAYED
        transaction.order = order
        transaction.blockHash = block?.headerHash

        if (block != null) {
            transaction.timestamp = block.timestamp
        }

        if (block != null && !block.hasTransactions) {
            block.hasTransactions = true
            storage.updateBlock(block)
        }
    }

    private class NotMineTransaction(val hash: ByteArray, val inBlock: Boolean) {
        override fun equals(other: Any?): Boolean {
            return other is NotMineTransaction && hash.contentEquals(other.hash) && inBlock == other.inBlock
        }

        override fun hashCode(): Int {
            return Objects.hash(hash.contentHashCode(), inBlock)
        }
    }

}
