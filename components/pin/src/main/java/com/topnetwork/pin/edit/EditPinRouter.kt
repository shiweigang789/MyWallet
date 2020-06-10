package com.topnetwork.pin.edit

import com.topnetwork.core.SingleLiveEvent

class EditPinRouter : EditPinModule.IRouter {

    val dismissWithSuccess = SingleLiveEvent<Unit>()

    override fun dismissModuleWithSuccess() {
        dismissWithSuccess.call()
    }
}
