package com.topnetwork.keystore

import android.os.Parcelable
import com.topnetwork.core.CoreApp
import kotlinx.android.parcel.Parcelize

/**
 * @FileName     : KeyStoreModule
 * @date         : 2020/6/9 11:31
 * @author       : Owen
 * @description  :
 */
object KeyStoreModule {

    const val MODE = "mode"

    interface IView {
        fun showNoSystemLockWarning()
        fun showInvalidKeyWarning()
        fun promptUserAuthentication()
    }

    interface IViewDelegate {
        fun viewDidLoad()
        fun onCloseInvalidKeyWarning()
        fun onAuthenticationCanceled()
        fun onAuthenticationSuccess()
    }

    interface IInteractor {
        val isSystemLockOff: Boolean
        val isKeyInvalidated: Boolean
        val isUserNotAuthenticated: Boolean

        fun resetApp()
        fun removeKey()
    }

    interface IInteractorDelegate

    interface IRouter {
        fun openLaunchModule()
        fun closeApplication()
    }

    fun init(view: KeyStoreViewModel, router: IRouter, mode: ModeType) {
        val interactor = KeyStoreInteractor(CoreApp.systemInfoManager, CoreApp.keyStoreManager)
        val presenter = KeyStorePresenter(interactor, router, mode)

        view.delegate = presenter
        presenter.view = view
        interactor.delegate = presenter
    }

    @Parcelize
    enum class ModeType : Parcelable {
        NoSystemLock,
        InvalidKey,
        UserAuthentication
    }

}