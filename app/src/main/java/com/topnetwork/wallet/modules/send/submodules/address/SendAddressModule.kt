package com.topnetwork.wallet.modules.send.submodules.address

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

}