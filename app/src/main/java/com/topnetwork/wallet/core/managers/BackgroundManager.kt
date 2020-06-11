package com.topnetwork.wallet.core.managers

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * @FileName     : BackgroundManager
 * @date         : 2020/6/11 18:06
 * @author       : Owen
 * @description  :
 */
class BackgroundManager(application: Application) : Application.ActivityLifecycleCallbacks {

    init {
        application.registerActivityLifecycleCallbacks(this)
    }

    interface Listener {
        fun willEnterForeground(activity: Activity) {}
        fun willEnterForeground() {}
        fun didEnterBackground() {}
    }

    private var refs: Int = 0
    private var listeners: MutableList<Listener> = ArrayList()

    val inForeground: Boolean
        get() = refs > 0

    val inBackground: Boolean
        get() = refs == 0

    @Synchronized
    fun registerListener(listener: Listener) {
        listeners.add(listener)
    }

    @Synchronized
    fun unregisterListener(listener: Listener) {
        listeners.remove(listener)
    }

    override fun onActivityPaused(activity: Activity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityStarted(activity: Activity) {
        if (refs == 0) {
            listeners.forEach { listener ->
                listener.willEnterForeground(activity)
                listener.willEnterForeground()
            }
        }
        refs++
    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {

    }

    override fun onActivityStopped(activity: Activity) {
        refs--

        if (refs == 0) {
            //App is in background
            listeners.forEach { listener ->
                listener.didEnterBackground()
            }

        }
    }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

}