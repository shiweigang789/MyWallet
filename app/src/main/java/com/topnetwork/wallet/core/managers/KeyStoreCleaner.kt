package com.topnetwork.wallet.core.managers

import com.topnetwork.core.IKeyStoreCleaner
import com.topnetwork.wallet.core.IAccountManager
import com.topnetwork.wallet.core.ILocalStorage
import com.topnetwork.wallet.core.IWalletManager

/**
 * @FileName     : KeyStoreCleaner
 * @date         : 2020/6/11 9:29
 * @author       : Owen
 * @description  :
 */
class KeyStoreCleaner(
    private val localStorage: ILocalStorage,
    private val accountManager: IAccountManager,
    private val walletManager: IWalletManager
) : IKeyStoreCleaner {
    override var encryptedSampleText: String?
        get() = localStorage.encryptedSampleText
        set(value) {
            localStorage.encryptedSampleText = value
        }

    override fun cleanApp() {
        accountManager.clear()
        walletManager.enable(listOf())
        localStorage.clear()
    }
}