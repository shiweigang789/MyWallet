package com.topnetwork.wallet.core.storage

import com.topnetwork.wallet.core.IEnabledWalletStorage
import com.topnetwork.wallet.entities.EnabledWallet

/**
 * @FileName     : EnabledWalletsStorage
 * @date         : 2020/6/11 11:33
 * @author       : Owen
 * @description  :
 */
class EnabledWalletsStorage(private val appDatabase: AppDatabase) : IEnabledWalletStorage {

    override val enabledWallets: List<EnabledWallet>
        get() = appDatabase.walletsDao().enabledCoins()

    override fun save(enabledWallets: List<EnabledWallet>) {
        appDatabase.walletsDao().insertWallets(enabledWallets)
    }

    override fun deleteAll() {
        appDatabase.walletsDao().deleteAll()
    }

    override fun delete(enabledWallets: List<EnabledWallet>) {
        appDatabase.walletsDao().deleteWallets(enabledWallets)
    }

}