package com.topnetwork.wallet.core

import android.util.Log
import androidx.preference.PreferenceManager
import com.topnetwork.core.CoreApp
import com.topnetwork.core.ICoreApp
import com.topnetwork.wallet.BuildConfig
import com.topnetwork.wallet.core.managers.AppConfigProvider
import com.topnetwork.wallet.core.managers.LocalStorageManager
import com.topnetwork.wallet.core.managers.SystemInfoManager
import io.reactivex.plugins.RxJavaPlugins
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @FileName     : App
 * @date         : 2020/6/10 15:12
 * @author       : Owen
 * @description  :
 */
class App : CoreApp() {


    override fun onCreate() {
        super.onCreate()
        if (!BuildConfig.DEBUG) {
            //Disable logging for lower levels in Release build
            Logger.getLogger("").level = Level.SEVERE
        }
        RxJavaPlugins.setErrorHandler { e: Throwable? ->
            Log.w("RxJava ErrorHandler", e)
        }

        instance = this
        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        val appConfig = AppConfigProvider()
        appConfigProvider = appConfig
        appConfigTestMode = appConfig
        languageConfigProvider = appConfig

        LocalStorageManager().apply {
            localStorage = this
            chartTypeStorage = this
            pinStorage = this
            themeStorage = this
        }

        systemInfoManager = SystemInfoManager()

    }

    companion object : ICoreApp by CoreApp {

        lateinit var localStorage: ILocalStorage
        lateinit var chartTypeStorage: IChartTypeStorage
        lateinit var appConfigProvider: IAppConfigProvider
        lateinit var accountManager: IAccountManager
        lateinit var numberFormatter: IAppNumberFormatter

    }
}