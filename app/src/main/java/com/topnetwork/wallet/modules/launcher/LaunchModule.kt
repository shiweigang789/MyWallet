package com.topnetwork.wallet.modules.launcher

import com.topnetwork.wallet.core.App

/**
 * @FileName     : LaunchModule
 * @date         : 2020/6/10 15:25
 * @author       : Owen
 * @description  : 启动界面
 */
object LaunchModule {

    interface IView

    interface IViewDelegate {
        fun viewDidLoad()
        fun didUnlock()
        fun didCancelUnlock()
    }

    interface IInteractor {
        val isPinNotSet: Boolean
        val isAccountsEmpty: Boolean
        val isSystemLockOff: Boolean
        val isKeyInvalidated: Boolean
        val isUserNotAuthenticated: Boolean
    }

    interface IInteractorDelegate

    interface IRouter {
        fun openWelcomeModule()
        fun openMainModule()
        fun openUnlockModule()
        fun closeApplication()
        fun openNoSystemLockModule()
        fun openKeyInvalidatedModule()
        fun openUserAuthenticationModule()
    }

    fun init(view: LaunchViewModel, router: IRouter) {
        val interactor = LaunchInteractor(App.accountManager, App.pinComponent, App.systemInfoManager, App.keyStoreManager)
        val presenter = LaunchPresenter(interactor, router)

        view.delegate = presenter
        presenter.view = view
        interactor.delegate = presenter
    }

}