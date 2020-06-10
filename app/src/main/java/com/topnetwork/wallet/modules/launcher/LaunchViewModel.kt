package com.topnetwork.wallet.modules.launcher

import androidx.lifecycle.ViewModel
import com.topnetwork.core.SingleLiveEvent

/**
 * @FileName     : LaunchViewModel
 * @date         : 2020/6/10 15:29
 * @author       : Owen
 * @description  : 启动界面
 */
class LaunchViewModel  : ViewModel(), LaunchModule.IView, LaunchModule.IRouter{

    lateinit var delegate: LaunchModule.IViewDelegate

    val openWelcomeModule = SingleLiveEvent<Void>()
    val openMainModule = SingleLiveEvent<Void>()
    val openUnlockModule = SingleLiveEvent<Void>()
    val openNoSystemLockModule = SingleLiveEvent<Void>()
    val openKeyInvalidatedModule = SingleLiveEvent<Void>()
    val openUserAuthenticationModule = SingleLiveEvent<Void>()
    val closeApplication = SingleLiveEvent<Void>()

    fun init() {
        LaunchModule.init(this, this)
        delegate.viewDidLoad()
    }

    override fun openWelcomeModule() {
        openWelcomeModule.call()
    }

    override fun openMainModule() {
        openMainModule.call()
    }

    override fun openUnlockModule() {
        openUnlockModule.call()
    }

    override fun closeApplication() {
        closeApplication.call()
    }

    override fun openNoSystemLockModule() {
        openNoSystemLockModule.call()
    }

    override fun openKeyInvalidatedModule() {
        openKeyInvalidatedModule.call()
    }

    override fun openUserAuthenticationModule() {
        openUserAuthenticationModule.call()
    }
}