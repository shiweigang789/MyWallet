package com.topnetwork.wallet.modules.send.submodules.hodler

import io.horizontalsystems.bitcoincore.core.IPluginData
import io.horizontalsystems.hodler.LockTimeInterval

/**
 * @FileName     : SendHodlerModule
 * @date         : 2020/6/10 18:21
 * @author       : Owen
 * @description  :
 */
object SendHodlerModule {

    interface IView {
        fun showLockTimeIntervalSelector(items: List<LockTimeIntervalViewItem>)
        fun setSelectedLockTimeInterval(timeInterval: LockTimeInterval?)
    }

    interface IViewDelegate {
        fun onViewDidLoad()
        fun onClickLockTimeInterval()
        fun onSelectLockTimeInterval(position: Int)
    }

    interface IInteractor {
        fun getLockTimeIntervals(): Array<LockTimeInterval>
    }

    interface IHodlerModule {
        fun pluginData(): Map<Byte, IPluginData>
    }

    interface IHodlerModuleDelegate {
        fun onUpdateLockTimeInterval(timeInterval: LockTimeInterval?)
    }

    data class LockTimeIntervalViewItem(val lockTimeInterval: LockTimeInterval?, val selected: Boolean)

}