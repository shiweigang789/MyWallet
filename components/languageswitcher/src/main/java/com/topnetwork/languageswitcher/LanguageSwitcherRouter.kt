package com.topnetwork.languageswitcher

import com.topnetwork.core.SingleLiveEvent

class LanguageSwitcherRouter : LanguageSwitcherModule.IRouter {
    val reloadAppLiveEvent = SingleLiveEvent<Unit>()
    val closeLiveEvent = SingleLiveEvent<Unit>()

    override fun reloadAppInterface() {
        reloadAppLiveEvent.call()
    }

    override fun close() {
        closeLiveEvent.call()
    }
}
