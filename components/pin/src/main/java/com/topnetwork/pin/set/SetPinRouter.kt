package com.topnetwork.pin.set

import com.topnetwork.core.SingleLiveEvent

class SetPinRouter : SetPinModule.IRouter {

    val navigateToMain = SingleLiveEvent<Unit>()
    val dismissWithSuccess = SingleLiveEvent<Unit>()

    override fun dismissModuleWithSuccess() {
        dismissWithSuccess.call()
    }

    override fun navigateToMain() {
        navigateToMain.call()
    }
}
