package com.topnetwork.wallet.modules.launcher

/**
 * @FileName     : LaunchPresenter
 * @date         : 2020/6/10 15:52
 * @author       : Owen
 * @description  :
 */
class LaunchPresenter(private val interactor: LaunchModule.IInteractor,
                      private val router: LaunchModule.IRouter) : LaunchModule.IViewDelegate, LaunchModule.IInteractorDelegate {

    var view: LaunchModule.IView? = null

    override fun viewDidLoad() {
        when {
            interactor.isSystemLockOff -> router.openNoSystemLockModule()
            interactor.isKeyInvalidated -> router.openKeyInvalidatedModule()
            interactor.isUserNotAuthenticated -> router.openUserAuthenticationModule()
            interactor.isAccountsEmpty -> router.openWelcomeModule()
            interactor.isPinNotSet -> router.openMainModule()
            else -> router.openUnlockModule()
        }
    }

    override fun didUnlock() {
        router.openMainModule()
    }

    override fun didCancelUnlock() {
        router.closeApplication()
    }
}