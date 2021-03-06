package com.topnetwork.wallet.modules.send.submodules.amount

import androidx.lifecycle.ViewModel
import com.topnetwork.wallet.entities.Coin
import com.topnetwork.wallet.entities.CoinValue
import com.topnetwork.wallet.entities.CurrencyValue
import com.topnetwork.wallet.modules.send.SendModule
import io.horizontalsystems.core.entities.Currency
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @FileName     : SendAmountPresenter
 * @date         : 2020/6/11 18:22
 * @author       : Owen
 * @description  :
 */
class SendAmountPresenter(
    val view: SendAmountModule.IView,
    private val interactor: SendAmountModule.IInteractor,
    private val presenterHelper: SendAmountPresenterHelper,
    private val coin: Coin,
    private val baseCurrency: Currency
) : ViewModel(), SendAmountModule.IViewDelegate, SendAmountModule.IInteractorDelegate,
    SendAmountModule.IAmountModule {

    var moduleDelegate: SendAmountModule.IAmountModuleDelegate? = null

    private var amount: BigDecimal? = null
    private var availableBalance: BigDecimal? = null
    private var minimumAmount: BigDecimal? = null
    private var maximumAmount: BigDecimal? = null
    private var minimumRequiredBalance: BigDecimal = BigDecimal.ZERO
    private var xRate: BigDecimal? = null

    override val currentAmount: BigDecimal
        get() = amount ?: BigDecimal.ZERO
    override var inputType = SendModule.InputType.COIN
        private set
    override val coinAmount: CoinValue
        get() = CoinValue(coin, amount ?: BigDecimal.ZERO)
    override val fiatAmount: CurrencyValue?
        get() {
            val currencyAmount = xRate?.let { amount?.times(it) }
            return currencyAmount?.let { CurrencyValue(baseCurrency, it) }
        }

    @Throws
    override fun primaryAmountInfo(): SendModule.AmountInfo {
        return when (inputType) {
            SendModule.InputType.COIN -> SendModule.AmountInfo.CoinValueInfo(
                CoinValue(
                    coin,
                    validAmount()
                )
            )
            SendModule.InputType.CURRENCY -> {
                this.xRate?.let { xRate ->
                    SendModule.AmountInfo.CurrencyValueInfo(
                        CurrencyValue(
                            baseCurrency,
                            validAmount() * xRate
                        )
                    )
                } ?: throw Exception("Invalid state")
            }
        }
    }

    override fun secondaryAmountInfo(): SendModule.AmountInfo? {
        return when (inputType.reversed()) {
            SendModule.InputType.COIN -> SendModule.AmountInfo.CoinValueInfo(
                CoinValue(
                    coin,
                    validAmount()
                )
            )
            SendModule.InputType.CURRENCY -> {
                this.xRate?.let { xRate ->
                    SendModule.AmountInfo.CurrencyValueInfo(
                        CurrencyValue(
                            baseCurrency,
                            validAmount() * xRate
                        )
                    )
                }
            }
        }
    }

    @Throws
    override fun validAmount(): BigDecimal {
        val amount = this.amount ?: BigDecimal.ZERO
        if (amount <= BigDecimal.ZERO) {
            throw SendAmountModule.ValidationError.EmptyValue("amount")
        }

        validate()

        return amount
    }

    override fun setLoading(loading: Boolean) {
        view.setLoading(loading)
    }

    override fun setAmount(amount: BigDecimal) {
        this.amount = amount

        syncAmount()
        syncHint()
        syncMaxButton()
        syncError()

        moduleDelegate?.onChangeAmount()
    }

    override fun setAvailableBalance(availableBalance: BigDecimal) {
        this.availableBalance = availableBalance

        syncMaxButton()
        syncAvailableBalance()
        syncError()
    }

    override fun setMinimumAmount(minimumAmount: BigDecimal) {
        this.minimumAmount = minimumAmount
    }

    override fun setMaximumAmount(maximumAmount: BigDecimal?) {
        this.maximumAmount = maximumAmount
    }

    override fun setMinimumRequiredBalance(minimumRequiredBalance: BigDecimal) {
        this.minimumRequiredBalance = minimumRequiredBalance
    }

    override fun onViewDidLoad() {
        xRate = interactor.getRate()

        inputType = when {
            xRate == null -> SendModule.InputType.COIN
            else -> interactor.defaultInputType
        }

        moduleDelegate?.onChangeInputType(inputType)

        syncAmountType()
        syncSwitchButton()
        syncAmount()
        syncHint()

        view.addTextChangeListener()
    }

    override fun onAmountChange(amountString: String) {
        val amount = amountString.toBigDecimalOrNull()
        val decimal = presenterHelper.decimal(inputType)

        if (amount != null && amount.scale() > decimal) {
            val amountNumber = amount.setScale(decimal, RoundingMode.FLOOR)
            val revertedInput = amountNumber.toPlainString()
            view.revertAmount(revertedInput)
        } else {
            this.amount = presenterHelper.getCoinAmount(amount, inputType, xRate)

            syncHint()
            syncMaxButton()
            syncError()

            moduleDelegate?.onChangeAmount()
        }
    }

    override fun onSwitchClick() {
        view.removeTextChangeListener()

        inputType = when (inputType) {
            SendModule.InputType.CURRENCY -> SendModule.InputType.COIN
            else -> SendModule.InputType.CURRENCY
        }
        interactor.defaultInputType = inputType
        moduleDelegate?.onChangeInputType(inputType)

        syncAmountType()
        syncAmount()
        syncHint()
        syncError()
        syncAvailableBalance()

        view.addTextChangeListener()
    }

    override fun onMaxClick() {
        amount = availableBalance?.subtract(minimumRequiredBalance)

        view.removeTextChangeListener()

        syncAmount()
        syncHint()
        syncMaxButton()
        syncError()

        moduleDelegate?.onChangeAmount()
        view.addTextChangeListener()
    }

    override fun didUpdateRate(rate: BigDecimal) {
        syncXRate(rate)
    }

    override fun willEnterForeground() {
        syncXRate(interactor.getRate())
    }

    override fun onCleared() {
        super.onCleared()
        interactor.onCleared()
    }

    private fun syncXRate(rate: BigDecimal?) {
        if (rate == xRate) {
            return
        }

        xRate = rate
        inputType = when (xRate) {
            null -> SendModule.InputType.COIN
            else -> interactor.defaultInputType
        }

        syncAmount()
        syncAvailableBalance()
        syncAmountType()
        syncHint()
        syncSwitchButton()
    }

    private fun syncAmount() {
        val amount = presenterHelper.getAmount(amount, inputType, xRate)
        view.setAmount(amount)
    }

    private fun syncAvailableBalance() {
        presenterHelper.getAvailableBalance(availableBalance, inputType, xRate)?.let {
            view.setAvailableBalance(it)
        }
    }

    private fun syncAmountType() {
        val prefix = presenterHelper.getAmountPrefix(inputType, xRate)
        view.setAmountType(prefix)
    }

    private fun syncHint() {
        val hint = presenterHelper.getHint(this.amount, inputType, xRate)
        view.setHint(hint)
    }

    private fun syncMaxButton() {
        val noneNullAvailableBalance = availableBalance ?: run {
            view.setMaxButtonVisible(false)
            return
        }

        amount?.let {
            if (it > BigDecimal.ZERO) {
                view.setMaxButtonVisible(false)
                return
            }
        }

        val hasSpendableBalance = noneNullAvailableBalance - minimumRequiredBalance > BigDecimal.ZERO
        view.setMaxButtonVisible(hasSpendableBalance)
    }

    private fun syncSwitchButton() {
        view.setSwitchButtonEnabled(xRate != null)
    }

    private fun syncError() {
        try {
            validate()
            view.setValidationError(null)

        } catch (e: SendAmountModule.ValidationError) {
            view.setValidationError(e)
        }
    }

    private fun validate() {
        val amount = this.amount ?: return
        if (amount <= BigDecimal.ZERO) return

        minimumAmount?.let {
            if (amount < it) throw SendAmountModule.ValidationError.TooFewAmount(amountInfo(it))
        }

        availableBalance?.let {
            if (amount > it) throw SendAmountModule.ValidationError.InsufficientBalance(
                amountInfo(
                    it
                )
            )

            if (it - amount < minimumRequiredBalance)
                throw SendAmountModule.ValidationError.NotEnoughForMinimumRequiredBalance(
                    CoinValue(
                        coin,
                        minimumRequiredBalance
                    )
                )
        }

        maximumAmount?.let {
            if (amount > it) throw SendAmountModule.ValidationError.MaxAmountLimit(amountInfo(it))
        }
    }

    private fun amountInfo(coinValue: BigDecimal): SendModule.AmountInfo? {
        return when (inputType) {
            SendModule.InputType.COIN -> {
                SendModule.AmountInfo.CoinValueInfo(CoinValue(coin, coinValue))
            }
            SendModule.InputType.CURRENCY -> {
                xRate?.let { rate ->
                    val value = coinValue.times(rate)
                    SendModule.AmountInfo.CurrencyValueInfo(CurrencyValue(baseCurrency, value))
                }
            }
        }
    }

}