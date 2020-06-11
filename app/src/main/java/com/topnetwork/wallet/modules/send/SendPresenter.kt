package com.topnetwork.wallet.modules.send

import androidx.lifecycle.ViewModel
import com.topnetwork.wallet.modules.send.submodules.address.SendAddressModule
import com.topnetwork.wallet.modules.send.submodules.amount.SendAmountModule
import com.topnetwork.wallet.modules.send.submodules.fee.SendFeeModule
import com.topnetwork.wallet.modules.send.submodules.hodler.SendHodlerModule

/**
 * @FileName     : SendPresenter
 * @date         : 2020/6/11 16:45
 * @author       : Owen
 * @description  :
 */
class SendPresenter(
    private val interactor: SendModule.ISendInteractor,
    val router: SendModule.IRouter)
    : ViewModel(), SendModule.IViewDelegate, SendModule.ISendInteractorDelegate, SendModule.ISendHandlerDelegate {

    var amountModuleDelegate: SendAmountModule.IAmountModuleDelegate? = null
    var addressModuleDelegate: SendAddressModule.IAddressModuleDelegate? = null
    var feeModuleDelegate: SendFeeModule.IFeeModuleDelegate? = null
    var hodlerModuleDelegate: SendHodlerModule.IHodlerModuleDelegate? = null

    override lateinit var view: SendModule.IView
    override lateinit var handler: SendModule.ISendHandler

    override fun onViewDidLoad() {
        view.loadInputItems(handler.inputItems)
    }

    override fun onModulesDidLoad() {
        handler.onModulesDidLoad()
    }

    override fun onAddressScan(address: String) {
        handler.onAddressScan(address)
    }

    override fun onProceedClicked() {
        view.showConfirmation(handler.confirmationViewItems())
    }

    override fun onSendConfirmed() {
        interactor.send(handler.sendSingle())
    }

    override fun onClear() {
        interactor.clear()
    }

    override fun onCleared() {
        interactor.clear()
        handler.onClear()
    }

    override fun sync() {
        handler.sync()
    }

    override fun didSend() {
        router.closeWithSuccess()
    }

    override fun didFailToSend(error: Throwable) {
        view.showErrorInToast(error)
    }

    override fun onChange(isValid: Boolean) {
        view.setSendButtonEnabled(isValid)
    }

}