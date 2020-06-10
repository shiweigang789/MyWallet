package com.topnetwork.pin.unlock

import com.topnetwork.core.SingleLiveEvent

class UnlockPinRouter : UnlockPinModule.IRouter {

    val dismissWithSuccess = SingleLiveEvent<Unit>()

    override fun dismissModuleWithSuccess() {
        dismissWithSuccess.call()
    }
}
