package com.topnetwork.wallet.modules.send

import androidx.lifecycle.MutableLiveData
import com.topnetwork.core.SingleLiveEvent
import com.topnetwork.wallet.core.LocalizedException

class SendView : SendModule.IView {

    override lateinit var delegate: SendModule.IViewDelegate

    val error = MutableLiveData<Throwable>()
    val errorInDialog = SingleLiveEvent<LocalizedException>()
    val confirmationViewItems = MutableLiveData<List<SendModule.SendConfirmationViewItem>>()
    val showSendConfirmation = SingleLiveEvent<Unit>()
    val sendButtonEnabled = MutableLiveData<Boolean>()
    val inputItems = SingleLiveEvent<List<SendModule.Input>>()

    override fun loadInputItems(inputs: List<SendModule.Input>) {
        inputItems.value = inputs
    }

    override fun setSendButtonEnabled(enabled: Boolean) {
        sendButtonEnabled.postValue(enabled)
    }

    override fun showErrorInToast(error: Throwable) {
        this.error.value = error
    }

    override fun showErrorInDialog(coinException: LocalizedException) {
        errorInDialog.value = coinException
    }

    override fun showConfirmation(confirmationViewItems: List<SendModule.SendConfirmationViewItem>) {
        this.confirmationViewItems.value = confirmationViewItems
        showSendConfirmation.call()
    }

}
