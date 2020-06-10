package com.topnetwork.wallet.modules.send

import com.topnetwork.wallet.core.App
import com.topnetwork.wallet.core.LocalizedException
import com.topnetwork.wallet.entities.CoinValue
import com.topnetwork.wallet.entities.CurrencyValue
import com.topnetwork.wallet.modules.send.submodules.address.SendAddressModule
import com.topnetwork.wallet.modules.send.submodules.amount.SendAmountModule
import com.topnetwork.wallet.modules.send.submodules.fee.SendFeeModule
import com.topnetwork.wallet.modules.send.submodules.hodler.SendHodlerModule
import com.topnetwork.wallet.modules.send.submodules.memo.SendMemoModule
import io.horizontalsystems.bitcoincore.core.IPluginData
import io.horizontalsystems.hodler.LockTimeInterval
import io.reactivex.Single
import java.math.BigDecimal

/**
 * @FileName     : SendModule
 * @date         : 2020/6/10 17:56
 * @author       : Owen
 * @description  :
 */
object SendModule {

    interface IView {
        var delegate: IViewDelegate

        fun loadInputItems(inputs: List<Input>)
        fun setSendButtonEnabled(enabled: Boolean)
        fun showConfirmation(confirmationViewItems: List<SendConfirmationViewItem>)
        fun showErrorInToast(error: Throwable)
        fun showErrorInDialog(coinException: LocalizedException)
    }

    interface IViewDelegate {
        var view: IView
        val handler: ISendHandler

        fun onViewDidLoad()
        fun onModulesDidLoad()
        fun onAddressScan(address: String)
        fun onProceedClicked()
        fun onSendConfirmed()
        fun onClear()
    }

    interface ISendBitcoinInteractor {
        val isLockTimeEnabled: Boolean

        fun fetchAvailableBalance(feeRate: Long, address: String?, pluginData: Map<Byte, IPluginData>?)
        fun fetchMinimumAmount(address: String?): BigDecimal
        fun fetchMaximumAmount(pluginData: Map<Byte, IPluginData>): BigDecimal?
        fun fetchFee(amount: BigDecimal, feeRate: Long, address: String?, pluginData: Map<Byte, IPluginData>?)
        fun validate(address: String, pluginData: Map<Byte, IPluginData>?)
        fun send(amount: BigDecimal, address: String, feeRate: Long, pluginData: Map<Byte, IPluginData>?): Single<Unit>
        fun clear()
    }

    interface ISendBitcoinInteractorDelegate {
        fun didFetchAvailableBalance(availableBalance: BigDecimal)
        fun didFetchFee(fee: BigDecimal)
    }

    interface ISendDashInteractor {
        fun fetchAvailableBalance(address: String?)
        fun fetchMinimumAmount(address: String?): BigDecimal
        fun fetchFee(amount: BigDecimal, address: String?)
        fun validate(address: String)
        fun send(amount: BigDecimal, address: String): Single<Unit>
        fun clear()
    }

    interface ISendDashInteractorDelegate {
        fun didFetchAvailableBalance(availableBalance: BigDecimal)
        fun didFetchFee(fee: BigDecimal)
    }

    interface ISendEthereumInteractor {
        val ethereumBalance: BigDecimal
        val minimumRequiredBalance: BigDecimal
        val minimumAmount: BigDecimal

        fun availableBalance(gasPrice: Long, gasLimit: Long?): BigDecimal
        fun validate(address: String)
        fun fee(gasPrice: Long, gasLimit: Long): BigDecimal
        fun send(amount: BigDecimal, address: String, gasPrice: Long, gasLimit: Long): Single<Unit>
        fun estimateGasLimit(toAddress: String, value: BigDecimal, gasPrice: Long?): Single<Long>

    }

    interface ISendBinanceInteractor {
        val availableBalance: BigDecimal
        val availableBinanceBalance: BigDecimal
        val fee: BigDecimal

        fun validate(address: String)
        fun send(amount: BigDecimal, address: String, memo: String?): Single<Unit>
    }

    interface ISendEosInteractor {
        val availableBalance: BigDecimal

        fun validate(account: String)
        fun send(amount: BigDecimal, account: String, memo: String?): Single<Unit>
    }

    interface IRouter {
        fun scanQrCode()
        fun closeWithSuccess()
    }

    interface ISendInteractor {
        var delegate: ISendInteractorDelegate

        fun send(sendSingle: Single<Unit>)
        fun clear()
    }

    interface ISendInteractorDelegate {
        fun sync()
        fun didSend()
        fun didFailToSend(error: Throwable)
    }

    abstract class SendConfirmationViewItem

    data class SendConfirmationAmountViewItem(val primaryInfo: AmountInfo,
                                              val secondaryInfo: AmountInfo?,
                                              val receiver: String,
                                              val locked: Boolean = false) : SendConfirmationViewItem()

    data class SendConfirmationFeeViewItem(val primaryInfo: AmountInfo,
                                           val secondaryInfo: AmountInfo?) : SendConfirmationViewItem()

    data class SendConfirmationTotalViewItem(val primaryInfo: AmountInfo,
                                             val secondaryInfo: AmountInfo?) : SendConfirmationViewItem()

    data class SendConfirmationMemoViewItem(val memo: String?) : SendConfirmationViewItem()

    data class SendConfirmationDurationViewItem(val duration: Long?) : SendConfirmationViewItem()

    data class SendConfirmationLockTimeViewItem(val lockTimeInterval: LockTimeInterval) : SendConfirmationViewItem()

    interface ISendHandler {
        var amountModule: SendAmountModule.IAmountModule
        var addressModule: SendAddressModule.IAddressModule
        var feeModule: SendFeeModule.IFeeModule
        var memoModule: SendMemoModule.IMemoModule
        var hodlerModule: SendHodlerModule.IHodlerModule?

        val inputItems: List<Input>
        var delegate: ISendHandlerDelegate

        fun sync()
        fun onModulesDidLoad()
        fun onAddressScan(address: String)
        fun onClear() {}

        @Throws
        fun confirmationViewItems(): List<SendConfirmationViewItem>
        fun sendSingle(): Single<Unit>
    }

    interface ISendHandlerDelegate {
        fun onChange(isValid: Boolean)
    }

    enum class InputType {
        COIN, CURRENCY;

        fun reversed(): InputType {
            return if (this == COIN) CURRENCY else COIN
        }
    }

    sealed class Input {
        object Amount : Input()
        class Address(val editable: Boolean = false) : Input()
        class Fee(val isAdjustable: Boolean) : Input()
        class Memo(val maxLength: Int) : Input()
        object ProceedButton : Input()
        object Hodler : Input()
    }

    sealed class AmountInfo {
        data class CoinValueInfo(val coinValue: CoinValue) : AmountInfo()
        data class CurrencyValueInfo(val currencyValue: CurrencyValue) : AmountInfo()

        fun getAmountName(): String = when (this) {
            is CoinValueInfo -> this.coinValue.coin.title
            is CurrencyValueInfo -> this.currencyValue.currency.code
        }

        fun getFormatted(): String? = when (this) {
            is CoinValueInfo -> {
                App.numberFormatter.format(this.coinValue)
            }
            is CurrencyValueInfo -> {
                App.numberFormatter.format(this.currencyValue, trimmable = true)
            }
        }
    }
}