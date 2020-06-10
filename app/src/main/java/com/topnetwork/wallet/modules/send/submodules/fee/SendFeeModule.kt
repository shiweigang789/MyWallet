package com.topnetwork.wallet.modules.send.submodules.fee

import com.topnetwork.wallet.core.FeeRatePriority
import com.topnetwork.wallet.entities.Coin
import com.topnetwork.wallet.entities.CoinValue
import com.topnetwork.wallet.entities.FeeRateInfo
import com.topnetwork.wallet.entities.FeeState
import com.topnetwork.wallet.modules.send.SendModule
import java.math.BigDecimal

/**
 * @FileName     : SendFeeModule
 * @date         : 2020/6/10 18:17
 * @author       : Owen
 * @description  :
 */
object SendFeeModule {

    class InsufficientFeeBalance(val coin: Coin, val coinProtocol: String, val feeCoin: Coin, val fee: CoinValue) :
        Exception()

    interface IView {
        fun setPrimaryFee(feeAmount: String?)
        fun setSecondaryFee(feeAmount: String?)
        fun setInsufficientFeeBalanceError(insufficientFeeBalance: InsufficientFeeBalance?)
        fun setDuration(duration: Long)
        fun setFeePriority(priority: FeeRatePriority)
        fun showFeeRatePrioritySelector(feeRates: List<FeeRateInfoViewItem>)

        fun setLoading(loading: Boolean)
        fun setFee(fee: SendModule.AmountInfo, convertedFee: SendModule.AmountInfo?)
        fun setError(error: Exception?)

    }

    interface IViewDelegate {
        fun onViewDidLoad()
        fun onChangeFeeRate(feeRateInfo: FeeRateInfo)
        fun onClickFeeRatePriority()
    }

    interface IInteractor {
        fun getRate(coinCode: String): BigDecimal?
        fun syncFeeRate()
        fun onClear()
    }

    interface IInteractorDelegate {
        fun didUpdate(feeRates: List<FeeRateInfo>)
        fun didReceiveError(error: Exception)
        fun didUpdateExchangeRate(rate: BigDecimal)
    }

    interface IFeeModule {
        val isValid: Boolean
        val feeRateState: FeeState
        val feeRate: Long
        val primaryAmountInfo: SendModule.AmountInfo
        val secondaryAmountInfo: SendModule.AmountInfo?
        val duration: Long?

        fun setLoading(loading: Boolean)
        fun setFee(fee: BigDecimal)
        fun setError(externalError: Exception?)
        fun setAvailableFeeBalance(availableFeeBalance: BigDecimal)
        fun setInputType(inputType: SendModule.InputType)
        fun fetchFeeRate()
    }

    interface IFeeModuleDelegate {
        fun onUpdateFeeRate()
    }

    data class FeeRateInfoViewItem(val feeRateInfo: FeeRateInfo, val selected: Boolean)

}