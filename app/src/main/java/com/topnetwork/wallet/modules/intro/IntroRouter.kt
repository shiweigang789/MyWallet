package com.topnetwork.wallet.modules.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.topnetwork.core.SingleLiveEvent

class IntroRouter : IntroModule.IRouter {
    val navigateToWelcomeLiveEvent = SingleLiveEvent<Unit>()
    
    override fun navigateToWelcome() {
        navigateToWelcomeLiveEvent.call()
    }

}
