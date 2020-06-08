package com.topnetwork.core

import android.content.SharedPreferences
import androidx.biometric.BiometricPrompt
import io.horizontalsystems.core.entities.Currency
import io.reactivex.Observable
import java.util.*
import javax.crypto.SecretKey

/**
 * @FileName     : Interfaces
 * @date         : 2020/6/8 14:26
 * @author       : Owen
 * @description  : 定义接口类
 */

interface ICoreApp {
    var preferences: SharedPreferences
    var appConfigTestMode: IAppConfigTestMode
    var languageConfigProvider: ILanguageConfigProvider
    var encryptionManager: IEncryptionManager
    var systemInfoManager: ISystemInfoManager
    var languageManager: ILanguageManager
    var currencyManager: ICurrencyManager
    var keyStoreManager: IKeyStoreManager
    var keyProvider: IKeyProvider
    var secureStorage: ISecuredStorage
    var pinComponent: IPinComponent
    var pinStorage: IPinStorage
    var themeStorage: IThemeStorage

    var instance: CoreApp
}

/**
 * 指纹验证
 */
interface IEncryptionManager {
    fun encrypt(data: String): String
    fun decrypt(data: String): String
    fun getCryptoObject(): BiometricPrompt.CryptoObject?
}

/**
 * 环境控制
 */
interface IAppConfigTestMode {
    val testMode: Boolean
}

/**
 * 语言信息
 */
interface ILanguageConfigProvider {
    val localizations: List<String>
}

/**
 * 系统信息
 */
interface ISystemInfoManager {
    val appVersion: String
    val isSystemLockOff: Boolean
    val biometricAuthSupported: Boolean
    val deviceModel: String
    val osVersion: String
}

/**
 * 密码存储
 */
interface ISecuredStorage {
    var isFingerprintEnabled: Boolean
    val savedPin: String?
    fun savePin(pin: String)
    fun removePin()
    fun pinIsEmpty(): Boolean
}

/**
 * 密码验证
 */
interface IPinComponent {
    var isFingerprintEnabled: Boolean
    val isPinSet: Boolean

    fun updateLastExitDateBeforeRestart()
    fun store(pin: String)
    fun validate(pin: String): Boolean
    fun clear()
    fun onUnlock()
}

/**
 * 语言设置
 */
interface ILanguageManager {
    var currentLocale: Locale
    var currentLanguage: String
    val currentLanguageName: String

    fun getName(language: String): String
    fun getNativeName(language: String): String
}

/**
 * 货币单位
 */
interface ICurrencyManager {
    var baseCurrency: Currency
    val baseCurrencyUpdatedSignal: Observable<Unit>
    val currencies: List<Currency>
}

interface IPinStorage {
    var failedAttempts: Int?
    var lockoutUptime: Long?
}

interface IThemeStorage {
    var isLightModeOn: Boolean
}

interface IKeyStoreManager {
    val isKeyInvalidated: Boolean
    val isUserNotAuthenticated: Boolean

    fun removeKey()
    fun resetApp()
}

interface IKeyStoreCleaner {
    var encryptedSampleText: String?
    fun cleanApp()
}

interface IKeyProvider {
    fun getKey(): SecretKey
}

interface ICurrentDateProvider {
    val currentDate: Date
}