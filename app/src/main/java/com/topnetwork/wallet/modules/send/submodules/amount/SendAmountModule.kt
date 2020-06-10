package com.topnetwork.wallet.modules.send.submodules.amount

import com.topnetwork.wallet.entities.CoinValue
import com.topnetwork.wallet.entities.CurrencyValue
import com.topnetwork.wallet.modules.send.SendModule
import java.math.BigDecimal

/**
 * @FileName     : SendAmountModule
 * @date         : 2020/6/10 18:13
 * @author       : Owen
 * @description  :
 */
object SendAmountModule {

    interface IView {

        fun setLoading(loading: Boolean)
        fun setAmountType(prefix: String?)
        fun setAmount(amount: String)
        fun setAvailableBalance(availableBalance: String)
        fun setHint(hint: String?)
        fun setValidationError(error: ValidationError?)

        fun setSwitchButtonEnabled(enabled: Boolean)
        fun setMaxButtonVisible(visible: Boolean)

        fun addTextChangeListener()
        fun removeTextChangeListener()

        fun revertAmount(amount: String)
    }

    interface IViewDelegate {
        fun onViewDidLoad()
        fun onAmountChange(amountString: String)
        fun onSwitchClick()
        fun onMaxClick()
    }

    interface IInteractor {
        var defaultInputType: SendModule.InputType
        fun getRate(): BigDecimal?
        fun onCleared()
    }

    interface IInteractorDelegate {
        fun didUpdateRate(rate: BigDecimal)
        fun willEnterForeground()
    }

    interface IAmountModule {

        val currentAmount: BigDecimal
        val inputType: SendModule.InputType
        val coinAmount: CoinValue
        val fiatAmount: CurrencyValue?

        @Throws
        fun primaryAmountInfo(): SendModule.AmountInfo

        @Throws
        fun secondaryAmountInfo(): SendModule.AmountInfo?

        @Throws
        fun validAmount(): BigDecimal

        fun setLoading(loading: Boolean)
        fun setAmount(amount: BigDecimal)
        fun setAvailableBalance(availableBalance: BigDecimal)
        fun setMinimumAmount(minimumAmount: BigDecimal)
        fun setMaximumAmount(maximumAmount: BigDecimal?)
        fun setMinimumRequiredBalance(minimumRequiredBalance: BigDecimal)
    }

    interface IAmountModuleDelegate {
        fun onChangeAmount()
        fun onChangeInputType(inputType: SendModule.InputType)
    }

    open class ValidationError : Exception() {
        class EmptyValue(val field: String) : ValidationError()
        class InsufficientBalance(val availableBalance: SendModule.AmountInfo?) : ValidationError()
        class NotEnoughForMinimumRequiredBalance(val minimumRequiredBalance: CoinValue) : ValidationError()
        class TooFewAmount(val minimumAmount: SendModule.AmountInfo?) : ValidationError()
        class MaxAmountLimit(val maximumAmount: SendModule.AmountInfo?) : ValidationError()
    }
}