package com.topnetwork.wallet.modules.launcher

import androidx.lifecycle.ViewModel
import com.topnetwork.core.SingleLiveEvent
import java.util.logging.Logger

/**
 * @FileName     : LaunchViewModel
 * @date         : 2020/6/10 15:29
 * @author       : Owen
 * @description  : 启动界面
 */
class LaunchViewModel  : ViewModel(), LaunchModule.IView, LaunchModule.IRouter{

    private val log = Logger.getLogger(LaunchViewModel::class.java.name)

    lateinit var delegate: LaunchModule.IViewDelegate

    val openWelcomeModule = SingleLiveEvent<Void>()
    val openMainModule = SingleLiveEvent<Void>()
    val openUnlockModule = SingleLiveEvent<Void>()
    val openNoSystemLockModule = SingleLiveEvent<Void>()
    val openKeyInvalidatedModule = SingleLiveEvent<Void>()
    val openUserAuthenticationModule = SingleLiveEvent<Void>()
    val closeApplication = SingleLiveEvent<Void>()

    fun init() {
        log.warning("init")
        LaunchModule.init(this, this)
        delegate.viewDidLoad()
    }

    override fun openWelcomeModule() {
        log.warning("openWelcomeModule")
        openWelcomeModule.call()
    }

    override fun openMainModule() {
        log.warning("openMainModule")
        openMainModule.call()
    }

    override fun openUnlockModule() {
        log.warning("openUnlockModule")
        openUnlockModule.call()
    }

    override fun closeApplication() {
        log.warning("closeApplication")
        closeApplication.call()
    }

    override fun openNoSystemLockModule() {
        log.warning("openNoSystemLockModule")
        openNoSystemLockModule.call()
    }

    override fun openKeyInvalidatedModule() {
        log.warning("openKeyInvalidatedModule")
        openKeyInvalidatedModule.call()
    }

    override fun openUserAuthenticationModule() {
        log.warning("openUserAuthenticationModule")
        openUserAuthenticationModule.call()
    }
}