package com.topnetwork.wallet.modules.send.submodules.amount

import com.topnetwork.wallet.entities.Wallet
import com.topnetwork.wallet.modules.send.SendModule
import com.topnetwork.wallet.modules.send.submodules.SendSubmoduleFragment

/**
 * @FileName     : SendAmountFragment
 * @date         : 2020/6/11 18:45
 * @author       : Owen
 * @description  :
 */
class SendAmountFragment(
    private val wallet: Wallet,
    private val amountModuleDelegate: SendAmountModule.IAmountModuleDelegate,
    private val sendHandler: SendModule.ISendHandler
) : SendSubmoduleFragment() {




    override fun init() {

    }
}