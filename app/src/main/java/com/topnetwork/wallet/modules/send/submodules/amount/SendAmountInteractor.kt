package com.topnetwork.wallet.modules.send.submodules.amount

import com.topnetwork.wallet.core.ILocalStorage
import com.topnetwork.wallet.core.IRateManager
import com.topnetwork.wallet.core.managers.BackgroundManager
import com.topnetwork.wallet.entities.Coin
import com.topnetwork.wallet.modules.send.SendModule
import io.horizontalsystems.core.entities.Currency
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal

/**
 * @FileName     : SendAmountInteractor
 * @date         : 2020/6/11 18:04
 * @author       : Owen
 * @description  :
 */
class SendAmountInteractor(
    private val baseCurrency: Currency,
    private val rateManager: IRateManager,
    private val localStorage: ILocalStorage,
    private val coin: Coin,
    private val backgroundManager: BackgroundManager
) : SendAmountModule.IInteractor, BackgroundManager.Listener {

    private val disposables = CompositeDisposable()
    var delegate: SendAmountModule.IInteractorDelegate? = null

    init {
        backgroundManager.registerListener(this)

        rateManager.marketInfoObservable(coin.code, baseCurrency.code)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe { marketInfo ->
                delegate?.didUpdateRate(marketInfo.rate)
            }
            .let {
                disposables.add(it)
            }
    }

    override var defaultInputType: SendModule.InputType
        get() = localStorage.sendInputType ?: SendModule.InputType.COIN
        set(value) {
            localStorage.sendInputType = value
        }

    override fun getRate(): BigDecimal? {
        return rateManager.getLatestRate(coin.code, baseCurrency.code)
    }

    override fun onCleared() {
        disposables.clear()
        backgroundManager.unregisterListener(this)
    }
}