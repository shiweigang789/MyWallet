package com.topnetwork.wallet.core.storage

import com.topnetwork.wallet.core.IAppConfigProvider
import com.topnetwork.wallet.core.IEnabledWalletStorage
import com.topnetwork.wallet.core.IWalletStorage
import com.topnetwork.wallet.entities.Account
import com.topnetwork.wallet.entities.Coin
import com.topnetwork.wallet.entities.EnabledWallet
import com.topnetwork.wallet.entities.Wallet

/**
 * @FileName     : WalletStorage
 * @date         : 2020/6/11 11:26
 * @author       : Owen
 * @description  :
 */
class WalletStorage(
    private val appConfigProvider: IAppConfigProvider,
    private val storage: IEnabledWalletStorage
) : IWalletStorage {
    override fun wallets(accounts: List<Account>): List<Wallet> {
        val coins = appConfigProvider.coins
        val enabledWallets = storage.enabledWallets
        return enabledWallets.map { enabledWallet ->
            val coin = coins.find { it.coinId == enabledWallet.coinId } ?: return@map null
            val account = accounts.find { it.id == enabledWallet.accountId } ?: return@map null
            Wallet(coin, account)
        }.mapNotNull { it }
    }

    override fun enabledCoins(): List<Coin> {
        val coins = appConfigProvider.coins

        return storage.enabledWallets.map { enabledWallet ->
            val coin = coins.find { it.coinId == enabledWallet.coinId }
            coin
        }.mapNotNull { it }
    }

    override fun save(wallets: List<Wallet>) {
        val enabledWallets = mutableListOf<EnabledWallet>()

        wallets.forEachIndexed { index, wallet ->

            enabledWallets.add(
                EnabledWallet(
                    wallet.coin.coinId,
                    wallet.account.id,
                    index
                )
            )
        }

        storage.save(enabledWallets)
    }

    override fun delete(wallets: List<Wallet>) {
        val enabledWallets = wallets.map { EnabledWallet(it.coin.coinId, it.account.id) }
        storage.delete(enabledWallets)
    }

    override fun wallet(account: Account, coin: Coin): Wallet? {
        val enabledWallets = storage.enabledWallets
        enabledWallets.firstOrNull { it.coinId == coin.coinId && it.accountId == account.id }?.let { enabledWallet ->
            return Wallet(coin, account)
        }
        return null
    }
}