package com.topnetwork.wallet.modules.send.submodules.memo

/**
 * @FileName     : SendMemoModule
 * @date         : 2020/6/10 18:20
 * @author       : Owen
 * @description  :
 */
object SendMemoModule {

    interface IView {
        fun setMaxLength(maxLength: Int)
    }

    interface IViewDelegate {
        fun onViewDidLoad()
        fun onTextEntered(memo: String)
    }

    interface IMemoModule {
        val memo: String?
    }

}