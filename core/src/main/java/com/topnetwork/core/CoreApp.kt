package com.topnetwork.core

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.topnetwork.core.helpers.LocaleHelper
import java.util.*

/**
 * @FileName     : CoreApp
 * @date         : 2020/6/8 14:40
 * @author       : Owen
 * @description  : Application基类
 */
abstract class CoreApp : Application() {

    companion object : ICoreApp {
        override lateinit var preferences: SharedPreferences
        override lateinit var appConfigTestMode: IAppConfigTestMode
        override lateinit var languageConfigProvider: ILanguageConfigProvider
        override lateinit var encryptionManager: IEncryptionManager
        override lateinit var systemInfoManager: ISystemInfoManager
        override lateinit var languageManager: ILanguageManager
        override lateinit var currencyManager: ICurrencyManager
        override lateinit var keyStoreManager: IKeyStoreManager
        override lateinit var keyProvider: IKeyProvider
        override lateinit var secureStorage: ISecuredStorage
        override lateinit var pinComponent: IPinComponent
        override lateinit var pinStorage: IPinStorage
        override lateinit var themeStorage: IThemeStorage

        override lateinit var instance: CoreApp
    }

    fun localeAwareContext(base: Context): Context {
        return LocaleHelper.onAttach(base)
    }

    fun getLocale(): Locale {
        return LocaleHelper.getLocale(this)
    }

    fun setLocale(currentLocale: Locale) {
        LocaleHelper.setLocale(this, currentLocale)
    }

    fun isLocaleRTL(): Boolean {
        return LocaleHelper.isRTL(Locale.getDefault())
    }

}