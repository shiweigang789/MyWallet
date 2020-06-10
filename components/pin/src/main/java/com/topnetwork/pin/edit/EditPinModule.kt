package com.topnetwork.pin.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.topnetwork.core.CoreApp
import com.topnetwork.pin.PinInteractor
import com.topnetwork.pin.PinView

/**
 * @FileName     : EditPinModule
 * @date         : 2020/6/10 11:22
 * @author       : Owen
 * @description  :
 */
class EditPinModule {

    interface IRouter {
        fun dismissModuleWithSuccess()
    }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val view = PinView()
            val router = EditPinRouter()

            val interactor = PinInteractor(CoreApp.pinComponent)
            val presenter = EditPinPresenter(view, router, interactor)

            interactor.delegate = presenter

            return presenter as T
        }
    }

}