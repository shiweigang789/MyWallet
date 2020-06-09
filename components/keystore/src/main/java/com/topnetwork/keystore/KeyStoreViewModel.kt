package com.topnetwork.keystore

import androidx.lifecycle.ViewModel
import com.topnetwork.core.SingleLiveEvent

/**
 * @FileName     : KeyStoreViewModel
 * @date         : 2020/6/9 13:58
 * @author       : Owen
 * @description  :
 */
class KeyStoreViewModel : ViewModel(), KeyStoreModule.IView, KeyStoreModule.IRouter {

    lateinit var delegate: KeyStoreModule.IViewDelegate

    val showNoSystemLockWarning = SingleLiveEvent<Void>()
    val showInvalidKeyWarning = SingleLiveEvent<Void>()
    val promptUserAuthentication = SingleLiveEvent<Void>()
    val openLaunchModule = SingleLiveEvent<Void>()
    val closeApplication = SingleLiveEvent<Void>()

    fun init(mode: KeyStoreModule.ModeType) {
        KeyStoreModule.init(this, this, mode)
        delegate.viewDidLoad()
    }

    override fun openLaunchModule() {

    }

    override fun closeApplication() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNoSystemLockWarning() {

    }

    override fun showInvalidKeyWarning() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun promptUserAuthentication() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}