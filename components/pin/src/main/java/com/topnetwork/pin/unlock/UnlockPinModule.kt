package com.topnetwork.pin.unlock

import androidx.biometric.BiometricPrompt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.topnetwork.core.CoreApp
import com.topnetwork.core.CurrentDateProvider
import com.topnetwork.pin.PinView
import com.topnetwork.pin.core.*

/**
 * @FileName     : UnlockPinModule
 * @date         : 2020/6/10 11:40
 * @author       : Owen
 * @description  :
 */
object UnlockPinModule {

    interface IRouter {
        fun dismissModuleWithSuccess()
    }

    interface IInteractor {
        val isFingerprintEnabled: Boolean
        val biometricAuthSupported: Boolean
        val cryptoObject: BiometricPrompt.CryptoObject?

        fun updateLockoutState()
        fun unlock(pin: String): Boolean
        fun onUnlock()
    }

    interface IInteractorDelegate {
        fun unlock()
        fun wrongPinSubmitted()
        fun updateLockoutState(state: LockoutState)
    }

    class Factory(private val showCancelButton: Boolean) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val view = PinView()
            val router = UnlockPinRouter()

            val lockoutManager = LockoutManager(CoreApp.pinStorage, UptimeProvider(), LockoutUntilDateFactory(
                CurrentDateProvider()
            ))
            val interactor = UnlockPinInteractor(CoreApp.pinComponent, lockoutManager, CoreApp.encryptionManager, CoreApp.systemInfoManager, OneTimeTimer())
            val presenter = UnlockPinPresenter(view, router, interactor, showCancelButton)

            interactor.delegate = presenter

            return presenter as T
        }
    }

}