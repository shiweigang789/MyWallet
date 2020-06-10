package com.topnetwork.wallet.core

import com.topnetwork.core.CoreApp
import com.topnetwork.core.ICoreApp

/**
 * @FileName     : App
 * @date         : 2020/6/10 15:12
 * @author       : Owen
 * @description  :
 */
class App : CoreApp() {

    companion object : ICoreApp by CoreApp {

        lateinit var accountManager: IAccountManager

    }
}