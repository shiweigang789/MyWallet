package com.topnetwork.wallet.core.managers

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.topnetwork.core.IPinStorage
import com.topnetwork.core.IThemeStorage
import com.topnetwork.wallet.core.App
import com.topnetwork.wallet.core.IChartTypeStorage
import com.topnetwork.wallet.core.ILocalStorage
import com.topnetwork.wallet.entities.AccountType
import com.topnetwork.wallet.entities.SyncMode
import com.topnetwork.wallet.entities.TransactionDataSortingType
import com.topnetwork.wallet.modules.balance.BalanceSortType
import com.topnetwork.wallet.modules.send.SendModule
import io.horizontalsystems.core.entities.AppVersion
import io.horizontalsystems.xrateskit.entities.ChartType

/**
 * @FileName     : LocalStorageManager
 * @date         : 2020/6/10 17:54
 * @author       : Owen
 * @description  :
 */
class LocalStorageManager : ILocalStorage, IThemeStorage, IPinStorage, IChartTypeStorage {

    private val LIGHT_MODE_ENABLED = "light_mode_enabled"
    private val SEND_INPUT_TYPE = "send_input_type"
    private val WORDLIST_BACKUP = "wordlist_backup"
    private val I_UNDERSTAND = "i_understand"
    private val BLOCK_TILL_DATE = "unblock_date"
    private val BASE_CURRENCY_CODE = "base_currency_code"
    private val FAILED_ATTEMPTS = "failed_attempts"
    private val LOCKOUT_TIMESTAMP = "lockout_timestamp"
    private val BASE_BITCOIN_PROVIDER = "base_bitcoin_provider"
    private val BASE_LITECOIN_PROVIDER = "base_litecoin_provider"
    private val BASE_ETHEREUM_PROVIDER = "base_ethereum_provider"
    private val BASE_DASH_PROVIDER = "base_dash_provider"
    private val BASE_BINANCE_PROVIDER = "base_binance_provider"
    private val BASE_EOS_PROVIDER = "base_eos_provider"
    private val SYNC_MODE = "sync_mode"
    private val SORT_TYPE = "balance_sort_type"
    private val CHART_TYPE = "prev_chart_type"
    private val APP_VERSIONS = "app_versions"
    private val ALERT_NOTIFICATION_ENABLED = "alert_notification"
    private val LOCK_TIME_ENABLED = "lock_time_enabled"
    private val ENCRYPTION_CHECKER_TEXT = "encryption_checker_text"
    private val BITCOIN_DERIVATION = "bitcoin_derivation"
    private val TOR_ENABLED = "tor_enabled"
    private val APP_LAUNCH_COUNT = "app_launch_count"
    private val RATE_APP_LAST_REQ_TIME = "rate_app_last_req_time"
    private val TRANSACTION_DATA_SORTING_TYPE = "transaction_data_sorting_type"
    private val BALANCE_HIDDEN = "balance_hidden"

    val gson by lazy { Gson() }

    override var isBackedUp: Boolean
        get() = App.preferences.getBoolean(WORDLIST_BACKUP, false)
        set(backedUp) {
            App.preferences.edit().putBoolean(WORDLIST_BACKUP, backedUp).apply()
        }
    override var sendInputType: SendModule.InputType?
        get() = App.preferences.getString(SEND_INPUT_TYPE, null)?.let {
            try {
                SendModule.InputType.valueOf(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        }
        set(value) {
            val editor = App.preferences.edit()
            when (value) {
                null -> editor.remove(SEND_INPUT_TYPE).apply()
                else -> editor.putString(SEND_INPUT_TYPE, value.name).apply()
            }
        }
    override var iUnderstand: Boolean
        get() = App.preferences.getBoolean(I_UNDERSTAND, false)
        set(value) {
            App.preferences.edit().putBoolean(I_UNDERSTAND, value).apply()
        }
    override var baseCurrencyCode: String?
        get() = App.preferences.getString(BASE_CURRENCY_CODE, null)
        set(value) {
            App.preferences.edit().putString(BASE_CURRENCY_CODE, value).apply()
        }
    override var blockTillDate: Long?
        get() {
            val date = App.preferences.getLong(BLOCK_TILL_DATE, 0)
            return if (date > 0) date else null
        }
        set(date) {
            date?.let {
                App.preferences.edit().putLong(BLOCK_TILL_DATE, date).apply()
            } ?: run {
                App.preferences.edit().remove(BLOCK_TILL_DATE).apply()
            }
        }
    override var baseBitcoinProvider: String?
        get() = App.preferences.getString(BASE_BITCOIN_PROVIDER, null)
        set(value) {
            App.preferences.edit().putString(BASE_BITCOIN_PROVIDER, value).apply()
        }
    override var baseLitecoinProvider: String?
        get() = App.preferences.getString(BASE_LITECOIN_PROVIDER, null)
        set(value) {
            App.preferences.edit().putString(BASE_LITECOIN_PROVIDER, value).apply()
        }

    override var baseEthereumProvider: String?
        get() = App.preferences.getString(BASE_ETHEREUM_PROVIDER, null)
        set(value) {
            App.preferences.edit().putString(BASE_ETHEREUM_PROVIDER, value).apply()
        }

    override var baseDashProvider: String?
        get() = App.preferences.getString(BASE_DASH_PROVIDER, null)
        set(value) {
            App.preferences.edit().putString(BASE_DASH_PROVIDER, value).apply()
        }

    override var baseBinanceProvider: String?
        get() = App.preferences.getString(BASE_BINANCE_PROVIDER, null)
        set(value) {
            App.preferences.edit().putString(BASE_BINANCE_PROVIDER, value).apply()
        }

    override var baseEosProvider: String?
        get() = App.preferences.getString(BASE_EOS_PROVIDER, null)
        set(value) {
            App.preferences.edit().putString(BASE_EOS_PROVIDER, value).apply()
        }
    override var syncMode: SyncMode?
        get() {
            val syncString = App.preferences.getString(SYNC_MODE, null)
            return syncString?.let { SyncMode.valueOf(it) }
        }
        set(syncMode) {
            App.preferences.edit().putString(SYNC_MODE, syncMode?.value).apply()
        }
    override var sortType: BalanceSortType
        get() {
            val sortString = App.preferences.getString(SORT_TYPE, null)
                ?: BalanceSortType.Name.getAsString()
            return BalanceSortType.getTypeFromString(sortString)
        }
        set(sortType) {
            App.preferences.edit().putString(SORT_TYPE, sortType.getAsString()).apply()
        }
    override var appVersions: List<AppVersion>
        get() {
            val versionsString = App.preferences.getString(APP_VERSIONS, null) ?: return listOf()
            val type = object : TypeToken<ArrayList<AppVersion>>() {}.type
            return gson.fromJson(versionsString, type)
        }
        set(value) {
            val versionsString = gson.toJson(value)
            App.preferences.edit().putString(APP_VERSIONS, versionsString).apply()
        }
    override var isAlertNotificationOn: Boolean
        get() = App.preferences.getBoolean(ALERT_NOTIFICATION_ENABLED, true)
        set(enabled) {
            App.preferences.edit().putBoolean(ALERT_NOTIFICATION_ENABLED, enabled).apply()
        }
    override var isLockTimeEnabled: Boolean
        get() = App.preferences.getBoolean(LOCK_TIME_ENABLED, false)
        set(enabled) {
            App.preferences.edit().putBoolean(LOCK_TIME_ENABLED, enabled).apply()
        }
    override var encryptedSampleText: String?
        get() = App.preferences.getString(ENCRYPTION_CHECKER_TEXT, null)
        set(encryptedText) {
            App.preferences.edit().putString(ENCRYPTION_CHECKER_TEXT, encryptedText).apply()
        }
    override var bitcoinDerivation: AccountType.Derivation?
        get() {
            val derivationString = App.preferences.getString(BITCOIN_DERIVATION, null)
            return derivationString?.let { AccountType.Derivation.valueOf(it) }
        }
        set(derivation) {
            App.preferences.edit().putString(BITCOIN_DERIVATION, derivation?.value).apply()
        }
    override var torEnabled: Boolean
        get() = App.preferences.getBoolean(TOR_ENABLED, false)
        @SuppressLint("ApplySharedPref")
        set(enabled) {
            //keep using commit() for synchronous storing
            App.preferences.edit().putBoolean(TOR_ENABLED, enabled).commit()
        }
    override var appLaunchCount: Int
        get() = App.preferences.getInt(APP_LAUNCH_COUNT, 0 )
        set(value) {
            App.preferences.edit().putInt(APP_LAUNCH_COUNT, value).apply()
        }
    override var rateAppLastRequestTime: Long
        get() = App.preferences.getLong(RATE_APP_LAST_REQ_TIME, 0)
        set(value) {
            App.preferences.edit().putLong(RATE_APP_LAST_REQ_TIME, value).apply()
        }
    override var transactionSortingType: TransactionDataSortingType
        get() {
            val txSortingTypeString = App.preferences.getString(TRANSACTION_DATA_SORTING_TYPE, null)
            return txSortingTypeString?.let { TransactionDataSortingType.valueOf(it) } ?: TransactionDataSortingType.Shuffle
        }
        set(sortingType) {
            App.preferences.edit().putString(TRANSACTION_DATA_SORTING_TYPE, sortingType.value).apply()
        }
    override var balanceHidden: Boolean
        get() = App.preferences.getBoolean(BALANCE_HIDDEN, false)
        set(value) {
            App.preferences.edit().putBoolean(BALANCE_HIDDEN, value).apply()
        }

    override fun clear() {
        App.preferences.edit().clear().apply()
    }

    override var failedAttempts: Int?
        get() {
            val attempts = App.preferences.getInt(FAILED_ATTEMPTS, 0)
            return when (attempts) {
                0 -> null
                else -> attempts
            }
        }
        set(value) {
            value?.let {
                App.preferences.edit().putInt(FAILED_ATTEMPTS, it).apply()
            } ?: App.preferences.edit().remove(FAILED_ATTEMPTS).apply()
        }
    override var lockoutUptime: Long?
        get() {
            val timestamp = App.preferences.getLong(LOCKOUT_TIMESTAMP, 0L)
            return when (timestamp) {
                0L -> null
                else -> timestamp
            }
        }
        set(value) {
            value?.let {
                App.preferences.edit().putLong(LOCKOUT_TIMESTAMP, it).apply()
            } ?: App.preferences.edit().remove(LOCKOUT_TIMESTAMP).apply()
        }
    override var isLightModeOn: Boolean
        get() = App.preferences.getBoolean(LIGHT_MODE_ENABLED, false)
        set(enabled) {
            App.preferences.edit().putBoolean(LIGHT_MODE_ENABLED, enabled).apply()
        }
    override var chartType: ChartType?
        get() {
            return ChartType.fromString(App.preferences.getString(CHART_TYPE, null))
        }
        set(mode) {
            App.preferences.edit().putString(CHART_TYPE, mode?.name).apply()
        }
}