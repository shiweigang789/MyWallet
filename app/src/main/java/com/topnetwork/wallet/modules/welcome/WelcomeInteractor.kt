package com.topnetwork.wallet.modules.welcome

import com.topnetwork.core.ISystemInfoManager

class WelcomeInteractor(private val systemInfoManager: ISystemInfoManager) : WelcomeModule.IInteractor {

    override val appVersion: String
        get() = systemInfoManager.appVersion
}
