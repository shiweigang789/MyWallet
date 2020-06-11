package com.topnetwork.wallet.core

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.topnetwork.core.CoreApp
import com.topnetwork.core.ICoreApp
import com.topnetwork.core.security.EncryptionManager
import com.topnetwork.core.security.KeyStoreManager
import com.topnetwork.languageswitcher.LanguageSettingsActivity
import com.topnetwork.pin.PinComponent
import com.topnetwork.pin.core.SecureStorage
import com.topnetwork.wallet.BuildConfig
import com.topnetwork.wallet.core.managers.*
import com.topnetwork.wallet.core.storage.AccountsStorage
import com.topnetwork.wallet.core.storage.AppDatabase
import com.topnetwork.wallet.core.storage.EnabledWalletsStorage
import com.topnetwork.wallet.core.storage.WalletStorage
import com.topnetwork.wallet.modules.launcher.LauncherActivity
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
        appDatabase = AppDatabase.getInstance(this)
        accountsStorage = AccountsStorage(appDatabase)
        accountCleaner = AccountCleaner(appConfigTestMode.testMode)
        accountManager = AccountManager(accountsStorage, accountCleaner)
        enabledWalletsStorage = EnabledWalletsStorage(appDatabase)
        walletStorage = WalletStorage(appConfigProvider, enabledWalletsStorage)
        walletManager = WalletManager(accountManager, walletStorage)

        KeyStoreManager(
            "MASTER_KEY",
            KeyStoreCleaner(localStorage, accountManager, walletManager)
        ).apply {
            keyStoreManager = this
            keyProvider = this
        }

        systemInfoManager = SystemInfoManager()
        encryptionManager = EncryptionManager(keyProvider)
        secureStorage = SecureStorage(encryptionManager)
        pinComponent = PinComponent(application = this,
            securedStorage = secureStorage,
            excludedActivityNames = listOf(LauncherActivity::class.java.name),
            onFire = { activity, requestCode ->
            })


        val nightMode = if (themeStorage.isLightModeOn)
            AppCompatDelegate.MODE_NIGHT_NO else
            AppCompatDelegate.MODE_NIGHT_YES

        if (AppCompatDelegate.getDefaultNightMode() != nightMode) {
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }

    companion object : ICoreApp by CoreApp {

        lateinit var appDatabase: AppDatabase

        lateinit var localStorage: ILocalStorage
        lateinit var chartTypeStorage: IChartTypeStorage
        lateinit var appConfigProvider: IAppConfigProvider
        lateinit var walletManager: IWalletManager
        lateinit var walletStorage: IWalletStorage
        lateinit var accountManager: IAccountManager
        lateinit var numberFormatter: IAppNumberFormatter

        lateinit var accountsStorage: IAccountsStorage
        lateinit var enabledWalletsStorage: IEnabledWalletStorage

        lateinit var accountCleaner: IAccountCleaner

    }
}