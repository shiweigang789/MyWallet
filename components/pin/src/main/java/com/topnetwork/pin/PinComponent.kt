package com.topnetwork.pin

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.topnetwork.core.IPinComponent
import com.topnetwork.core.ISecuredStorage
import com.topnetwork.pin.core.LockManager
import com.topnetwork.pin.core.PinManager

/**
 * @FileName     : PinComponent
 * @date         : 2020/6/10 11:02
 * @author       : Owen
 * @description  :
 */
class PinComponent(
    application: Application,
    securedStorage: ISecuredStorage,
    private val excludedActivityNames: List<String>,
    private val onFire: (activity: Activity, requestCode: Int) -> Unit
) : Application.ActivityLifecycleCallbacks, IPinComponent {

    init {
        application.registerActivityLifecycleCallbacks(this)
    }

    private var refs: Int = 0
    private var foregroundActivity: Activity? = null
    private val pinManager = PinManager(securedStorage)
    private val appLockManager = LockManager(pinManager, application.applicationContext)

    override var isFingerprintEnabled: Boolean
        get() = pinManager.isFingerprintEnabled
        set(value) {
            pinManager.isFingerprintEnabled = value
        }

    override val isPinSet: Boolean
        get() = pinManager.isPinSet

    override fun updateLastExitDateBeforeRestart() {
        appLockManager.updateLastExitDate()
    }

    override fun store(pin: String) {
        pinManager.store(pin)
    }

    override fun validate(pin: String): Boolean {
        return pinManager.validate(pin)
    }

    override fun clear() {
        pinManager.clear()
    }

    override fun onUnlock() {
        appLockManager.onUnlock()
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {

    }

    override fun onActivityResumed(activity: Activity) {
        foregroundActivity = activity

        if (appLockManager.isLocked && !excludedActivityNames.contains(activity::class.java.name)) {
            onFire.invoke(activity, 1)
        }
    }

    override fun onActivityPaused(p0: Activity) {
        foregroundActivity = null
    }

    override fun onActivityStarted(p0: Activity) {
        if (refs == 0) {
            appLockManager.willEnterForeground()
        }
        refs++
    }

    override fun onActivityStopped(p0: Activity) {
        refs--

        if (refs == 0) {
            appLockManager.didEnterBackground()
        }
    }

    override fun onActivityDestroyed(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }

}