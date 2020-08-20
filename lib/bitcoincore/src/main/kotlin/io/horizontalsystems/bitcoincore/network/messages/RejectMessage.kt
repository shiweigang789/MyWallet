package io.horizontalsystems.bitcoincore.network.messages

import io.horizontalsystems.bitcoincore.io.BitcoinInput
import java.io.ByteArrayInputStream

class RejectMessage(private val responseToMessage: String,
                    private val rejectCode: Byte,
                    private val reason: String) : IMessage {

    override fun toString(): String {
        return "RejectMessage(responseToMessage=$responseToMessage, rejectCode: $rejectCode, reason: $reason)"
    }
}

class RejectMessageParser : IMessageParser {
    override val command = "reject"

    override fun parseMessage(payload: ByteArray): IMessage {
        BitcoinInput(ByteArrayInputStream(payload)).use { input ->
            val responseToMessage = input.readString()
            val rejectCode = input.readByte()
            val reason = input.readString()

            return RejectMessage(responseToMessage, rejectCode, reason)
        }
    }

}
