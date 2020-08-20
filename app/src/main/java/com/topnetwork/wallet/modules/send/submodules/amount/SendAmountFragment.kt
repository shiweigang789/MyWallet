package com.topnetwork.wallet.modules.send.submodules.amount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.topnetwork.wallet.R
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

    private lateinit var presenter: SendAmountPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.view_amount_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        presenter = ViewModelProvider(this, SendAmountModule.Factory(wallet, sendHandler))
//            .get(SendAmountPresenter::class.java)
//        val presenterView = presenter.view as SendAmountView
//        presenter.moduleDelegate = amountModuleDelegate

    }

    override fun init() {

    }
}