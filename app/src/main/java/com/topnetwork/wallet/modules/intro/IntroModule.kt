package com.topnetwork.wallet.modules.intro

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @FileName     : IntroModule
 * @date         : 2020/6/11 14:35
 * @author       : Owen
 * @description  :
 */
object IntroModule {

    interface IRouter {
        fun navigateToWelcome()
    }

    fun start(context: Context) {
        val intent = Intent(context, IntroActivity::class.java)
        context.startActivity(intent)
    }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val presenter = IntroPresenter(IntroRouter())

            return presenter as T
        }
    }

}