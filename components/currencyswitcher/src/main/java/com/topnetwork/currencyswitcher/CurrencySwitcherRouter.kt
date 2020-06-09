package com.topnetwork.currencyswitcher

import com.topnetwork.core.SingleLiveEvent

class CurrencySwitcherRouter : CurrencySwitcherModule.IRouter {
    val closeLiveEvent = SingleLiveEvent<Unit>()

    override fun close() {
        closeLiveEvent.call()
    }
}
