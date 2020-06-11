package com.topnetwork.wallet.modules.send.submodules.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.topnetwork.wallet.core.App
import com.topnetwork.wallet.entities.Coin
import com.topnetwork.wallet.modules.send.SendModule
import com.topnetwork.wallet.ui.helpers.TextHelper
import java.math.BigDecimal

/**
 * @FileName     : SendAddressModule
 * @date         : 2020/6/10 18:17
 * @author       : Owen
 * @description  :
 */
object SendAddressModule {

    interface IView {
        fun setAddress(address: String?)
        fun setAddressError(error: Exception?)
        fun setAddressInputAsEditable(editable: Boolean)
    }

    interface IViewDelegate {
        fun onViewDidLoad()
        fun onAddressPasteClicked()
        fun onAddressDeleteClicked()
        fun onAddressScanClicked()
        fun onManualAddressEnter(addressText: String)
    }

    interface IInteractor {
        val addressFromClipboard: String?

        fun parseAddress(address: String): Pair<String, BigDecimal?>
    }

    interface IInteractorDelegate

    interface IAddressModule {
        val currentAddress: String?

        @Throws
        fun validAddress(): String

        fun didScanQrCode(address: String)
    }

    interface IAddressModuleDelegate {
        fun validate(address: String)

        fun onUpdateAddress()
        fun onUpdateAmount(amount: BigDecimal)

        fun scanQrCode()
    }

    open class ValidationError : Exception() {
        class InvalidAddress : ValidationError()
    }

    class Factory(
        private val coin: Coin,
        private val editable: Boolean,
        private val sendHandler: SendModule.ISendHandler
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            val view = SendAddressView()
            val addressParser = App.addressParserFactory.parser(coin)
            val interactor = SendAddressInteractor(TextHelper, addressParser)
            val presenter = SendAddressPresenter(view, editable, interactor)

            interactor.delegate = presenter
            sendHandler.addressModule = presenter

            return presenter as T
        }
    }

}