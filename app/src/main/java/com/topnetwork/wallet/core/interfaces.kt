package com.topnetwork.wallet.core

import com.topnetwork.wallet.entities.Account
import com.topnetwork.wallet.entities.CoinType
import io.reactivex.Flowable

/**
 * @FileName     : interfaces
 * @date         : 2020/6/10 15:33
 * @author       : Owen
 * @description  :
 */


interface IAccountManager {
    val isAccountsEmpty: Boolean
    val accounts: List<Account>
    val accountsFlowable: Flowable<List<Account>>
    val deleteAccountObservable: Flowable<String>

    fun account(coinType: CoinType): Account?
    fun loadAccounts()
    fun save(account: Account)
    fun update(account: Account)
    fun delete(id: String)
    fun clear()
    fun clearAccounts()
}