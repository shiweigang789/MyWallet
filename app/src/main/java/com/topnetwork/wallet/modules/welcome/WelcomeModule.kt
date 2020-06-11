package com.topnetwork.wallet.modules.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.topnetwork.wallet.core.App

/**
 * @FileName     : WelcomeModule
 * @date         : 2020/6/11 15:46
 * @author       : Owen
 * @description  :
 */
object WelcomeModule {

    interface IView {
        fun setAppVersion(appVersion: String)
    }

    interface IViewDelegate {
        fun createWalletDidClick()
        fun restoreWalletDidClick()
        fun viewDidLoad()
        fun openTorPage()
    }

    interface IInteractor {
        val appVersion: String
    }

    interface IRouter {
        fun openRestoreModule()
        fun openCreateWalletModule()
        fun openTorPage()
    }

    fun start(context: Context, options: Bundle?) {
        val intent = Intent(context, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        context.startActivity(intent, options)
    }

    fun init(view: WelcomeViewModel, router: IRouter) {
        val interactor = WelcomeInteractor(App.systemInfoManager)
        val presenter = WelcomePresenter(interactor, router)

        view.delegate = presenter
        presenter.view = view
    }

}