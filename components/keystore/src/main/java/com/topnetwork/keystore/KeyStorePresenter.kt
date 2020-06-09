package com.topnetwork.keystore

/**
 * @FileName     : KeyStorePresenter
 * @date         : 2020/6/9 14:16
 * @author       : Owen
 * @description  :
 */
class KeyStorePresenter(
    private val interactor: KeyStoreModule.IInteractor,
    private val router: KeyStoreModule.IRouter,
    private val mode: KeyStoreModule.ModeType
) : KeyStoreModule.IViewDelegate, KeyStoreModule.IInteractorDelegate {

    var view: KeyStoreModule.IView? = null

    override fun viewDidLoad() {
        when (mode) {
            KeyStoreModule.ModeType.NoSystemLock -> {
                interactor.resetApp()
                view?.showNoSystemLockWarning()
            }
            KeyStoreModule.ModeType.InvalidKey -> {
                interactor.resetApp()
                view?.showInvalidKeyWarning()
            }
            KeyStoreModule.ModeType.UserAuthentication -> {
                view?.promptUserAuthentication()
            }
        }
    }

    override fun onCloseInvalidKeyWarning() {
        interactor.removeKey()
        router.openLaunchModule()
    }

    override fun onAuthenticationCanceled() {
        router.closeApplication()
    }

    override fun onAuthenticationSuccess() {
        router.openLaunchModule()
    }
}