package com.topnetwork.keystore

import com.topnetwork.core.IKeyStoreManager
import com.topnetwork.core.ISystemInfoManager

/**
 * @FileName     : KeyStoreInteractor
 * @date         : 2020/6/9 14:12
 * @author       : Owen
 * @description  :
 */
class KeyStoreInteractor(
    private val systemInfoManager: ISystemInfoManager,
    private val keyStoreManager: IKeyStoreManager
) : KeyStoreModule.IInteractor {

    var delegate: KeyStoreModule.IInteractorDelegate? = null

    override val isSystemLockOff: Boolean
        get() = systemInfoManager.isSystemLockOff
    override val isKeyInvalidated: Boolean
        get() = keyStoreManager.isKeyInvalidated
    override val isUserNotAuthenticated: Boolean
        get() = keyStoreManager.isUserNotAuthenticated

    override fun resetApp() {
        keyStoreManager.resetApp()
    }

    override fun removeKey() {
        keyStoreManager.removeKey()
    }
}