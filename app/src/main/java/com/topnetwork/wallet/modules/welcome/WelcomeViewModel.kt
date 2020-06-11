package com.topnetwork.wallet.modules.welcome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.topnetwork.core.SingleLiveEvent

/**
 * @FileName     : WelcomeViewModel
 * @date         : 2020/6/11 15:48
 * @author       : Owen
 * @description  :
 */
class WelcomeViewModel: ViewModel(), WelcomeModule.IView, WelcomeModule.IRouter {

    lateinit var delegate: WelcomeModule.IViewDelegate

    val openRestoreModule = SingleLiveEvent<Void>()
    val openCreateWalletModule = SingleLiveEvent<Void>()
    val openTorPage = SingleLiveEvent<Void>()
    val appVersionLiveData = MutableLiveData<String>()

    fun init() {
        WelcomeModule.init(this, this)
        delegate.viewDidLoad()
    }

    override fun setAppVersion(appVersion: String) {
        appVersionLiveData.value = appVersion
    }

    override fun openRestoreModule() {
        openRestoreModule.call()
    }

    override fun openCreateWalletModule() {
        openCreateWalletModule.call()
    }

    override fun openTorPage() {
        openTorPage.call()
    }
}