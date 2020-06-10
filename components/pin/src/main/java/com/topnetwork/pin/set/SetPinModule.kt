package com.topnetwork.pin.set

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.topnetwork.core.CoreApp
import com.topnetwork.pin.PinInteractor
import com.topnetwork.pin.PinView

object SetPinModule {

    interface IRouter {
        fun navigateToMain()
        fun dismissModuleWithSuccess()
    }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val view = PinView()
            val router = SetPinRouter()

            val interactor = PinInteractor(CoreApp.pinComponent)
            val presenter = SetPinPresenter(view, router, interactor)

            interactor.delegate = presenter

            return presenter as T
        }
    }

}
