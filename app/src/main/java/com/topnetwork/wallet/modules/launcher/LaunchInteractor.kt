package com.topnetwork.wallet.modules.launcher

import com.topnetwork.core.IKeyStoreManager
import com.topnetwork.core.IPinComponent
import com.topnetwork.core.ISystemInfoManager
import com.topnetwork.wallet.core.IAccountManager

/**
 * @FileName     : LaunchInteractor
 * @date         : 2020/6/10 15:32
 * @author       : Owen
 * @description  :
 */
class LaunchInteractor(
    private val accountManager: IAccountManager,
    private val pinComponent: IPinComponent,
    private val systemInfoManager: ISystemInfoManager,
    private val keyStoreManager: IKeyStoreManager
) : LaunchModule.IInteractor {

    var delegate: LaunchModule.IInteractorDelegate? = null

    override val isPinNotSet: Boolean
        get() = !pinComponent.isPinSet
    override val isAccountsEmpty: Boolean
        get() = accountManager.isAccountsEmpty
    override val isSystemLockOff: Boolean
        get() = systemInfoManager.isSystemLockOff
    override val isKeyInvalidated: Boolean
        get() = keyStoreManager.isKeyInvalidated
    override val isUserNotAuthenticated: Boolean
        get() = keyStoreManager.isUserNotAuthenticated

}