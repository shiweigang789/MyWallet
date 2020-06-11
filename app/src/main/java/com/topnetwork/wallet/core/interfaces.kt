package com.topnetwork.wallet.core

import android.content.Context
import android.text.SpannableString
import com.topnetwork.wallet.entities.*
import com.topnetwork.wallet.modules.balance.BalanceSortType
import com.topnetwork.wallet.modules.send.SendModule
import io.horizontalsystems.core.entities.AppVersion
import io.horizontalsystems.core.entities.Currency
import io.horizontalsystems.xrateskit.entities.ChartType
import io.reactivex.Flowable
import io.reactivex.Observable
import java.math.BigDecimal

/**
 * @FileName     : interfaces
 * @date         : 2020/6/10 15:33
 * @author       : Owen
 * @description  :
 */

interface ILocalStorage {
    var isBackedUp: Boolean
    var sendInputType: SendModule.InputType?
    var iUnderstand: Boolean
    var baseCurrencyCode: String?
    var blockTillDate: Long?

    var baseBitcoinProvider: String?
    var baseLitecoinProvider: String?
    var baseEthereumProvider: String?
    var baseDashProvider: String?
    var baseBinanceProvider: String?
    var baseEosProvider: String?
    var syncMode: SyncMode?
    var sortType: BalanceSortType
    var appVersions: List<AppVersion>
    var isAlertNotificationOn: Boolean
    var isLockTimeEnabled: Boolean
    var encryptedSampleText: String?
    var bitcoinDerivation: AccountType.Derivation?
    var torEnabled: Boolean
    var appLaunchCount: Int
    var rateAppLastRequestTime: Long
    var transactionSortingType: TransactionDataSortingType
    var balanceHidden: Boolean

    fun clear()
}

interface IChartTypeStorage {
    var chartType: ChartType?
}

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

interface IAppConfigProvider {
    val companyWebPageLink: String
    val appWebPageLink: String
    val reportEmail: String
    val walletHelpTelegramGroup: String
    val ipfsId: String
    val ipfsMainGateway: String
    val ipfsFallbackGateway: String
    val infuraProjectId: String?
    val infuraProjectSecret: String?
    val fiatDecimal: Int
    val maxDecimal: Int
    val currencies: List<Currency>
    val featuredCoins: List<Coin>
    val coins: List<Coin>
    val derivationSettings: List<DerivationSetting>
    val syncModeSettings: List<SyncModeSetting>
    val communicationSettings: List<CommunicationSetting>
}

interface IAccountsStorage {
    val isAccountsEmpty: Boolean

    fun allAccounts(): List<Account>
    fun save(account: Account)
    fun update(account: Account)
    fun delete(id: String)
    fun getNonBackedUpCount(): Flowable<Int>
    fun clear()
    fun getDeletedAccountIds(): List<String>
    fun clearDeleted()
}

interface IAccountCleaner {
    fun clearAccounts(accountIds: List<String>)
    fun clearAccount(coinType: CoinType, accountId: String)
}

interface IWalletStorage {
    fun wallets(accounts: List<Account>): List<Wallet>
    fun enabledCoins(): List<Coin>
    fun save(wallets: List<Wallet>)
    fun delete(wallets: List<Wallet>)
    fun wallet(account: Account, coin: Coin): Wallet?
}

interface IAppNumberFormatter {
    fun format(coinValue: CoinValue, explicitSign: Boolean = false, realNumber: Boolean = false, trimmable: Boolean = false): String?
    fun format(currencyValue: CurrencyValue, showNegativeSign: Boolean = true, trimmable: Boolean = false, canUseLessSymbol: Boolean = true): String?
    fun formatForRates(currencyValue: CurrencyValue, trimmable: Boolean = false, maxFraction: Int? = null): String?
    fun formatForTransactions(coinValue: CoinValue): String?
    fun formatForTransactions(context: Context, currencyValue: CurrencyValue, isIncoming: Boolean, canUseLessSymbol: Boolean, trimmable: Boolean): SpannableString
    fun format(value: Double, showSign: Boolean = false, precision: Int = 8): String
    fun format(value: BigDecimal, precision: Int): String?
}

interface IEnabledWalletStorage {
    val enabledWallets: List<EnabledWallet>
    fun save(enabledWallets: List<EnabledWallet>)
    fun delete(enabledWallets: List<EnabledWallet>)
    fun deleteAll()
}

interface IWalletManager {
    val wallets: List<Wallet>
    val walletsUpdatedObservable: Observable<List<Wallet>>
    fun wallet(coin: Coin): Wallet?

    fun loadWallets()
    fun enable(wallets: List<Wallet>)
    fun save(wallets: List<Wallet>)
    fun delete(wallets: List<Wallet>)
    fun clear()
}

enum class FeeRatePriority(val value: Int) {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    companion object {
        fun valueOf(value: Int): FeeRatePriority = values().find { it.value == value } ?: MEDIUM
    }
}