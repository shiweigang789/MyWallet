package io.horizontalsystems.bitcoincore.network.peer

import io.horizontalsystems.bitcoincore.io.BitcoinInput
import io.horizontalsystems.bitcoincore.network.Network
import io.horizontalsystems.bitcoincore.network.messages.IMessage
import io.horizontalsystems.bitcoincore.network.messages.NetworkMessageParser
import io.horizontalsystems.bitcoincore.network.messages.NetworkMessageSerializer
import io.horizontalsystems.bitcoincore.utils.NetworkUtils
import java.io.IOException
import java.io.OutputStream
import java.net.*
import java.util.concurrent.ExecutorService
import java.util.logging.Logger

class PeerConnection(
        private val host: String,
        private val network: Network,
        private val listener: Listener,
        private val sendingExecutor: ExecutorService,
        private val networkMessageParser: NetworkMessageParser,
        private val networkMessageSerializer: NetworkMessageSerializer)
    : Runnable {

    interface Listener {
        fun socketConnected(address: InetAddress)
        fun disconnected(e: Exception? = null)
        fun onTimePeriodPassed() // didn't find better name
        fun onMessage(message: IMessage)
    }

    private val socket = NetworkUtils.createSocket()

    private val logger = Logger.getLogger("Peer[$host]")
    private var outputStream: OutputStream? = null
    private var disconnectError: Exception? = null

    @Volatile
    private var isRunning = false

    override fun run() {
        isRunning = true
        // connect:
        try {
            socket.connect(InetSocketAddress(host, network.port), 10000)
            socket.soTimeout = 10000

            outputStream = socket.getOutputStream()
            val inStream = socket.getInputStream()
            val inputStream = BitcoinInput(inStream)

            logger.info("Socket $host connected.")

            listener.socketConnected(socket.inetAddress)
            // loop:
            while (isRunning) {
                listener.onTimePeriodPassed()

                Thread.sleep(1000)

                // try receive message:
                while (isRunning && inStream.available() > 0) {
                    val parsedMsg = networkMessageParser.parseMessage(inputStream)
                    logger.info("<= $parsedMsg")
                    listener.onMessage(parsedMsg)
                }
            }

            listener.disconnected(disconnectError)
        } catch (e: SocketTimeoutException) {
            logger.warning("Socket timeout exception: ${e.message}")
            listener.disconnected(e)
        } catch (e: ConnectException) {
            logger.warning("Connect exception: ${e.message}")
            listener.disconnected(e)
        } catch (e: IOException) {
            logger.warning("IOException: ${e.message}")
            listener.disconnected(e)
        } catch (e: InterruptedException) {
            logger.warning("Peer connection thread interrupted: ${e.message}")
            listener.disconnected()
        } catch (e: Exception) {
            logger.warning("Peer connection exception: ${e.message}")
            listener.disconnected(e)
        } finally {
            isRunning = false
        }
    }

    fun close(error: Exception?) {
        disconnectError = error
        outputStream = null
        isRunning = false
    }

    @Synchronized
    fun sendMessage(message: IMessage) {
        sendingExecutor.execute {
            try {
                logger.info("=> $message")
                outputStream?.write(networkMessageSerializer.serialize(message))
            } catch (e: Exception) {
                close(e)
            }
        }
    }

}
