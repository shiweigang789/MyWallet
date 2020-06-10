package com.topnetwork.pin.core

import com.topnetwork.core.IPinStorage

/**
 * @FileName     : LockoutManager
 * @date         : 2020/6/10 10:06
 * @author       : Owen
 * @description  :
 */
class LockoutManager(
    private val localStorage: IPinStorage,
    private val uptimeProvider: UptimeProvider,
    private val lockoutUntilDateFactory: ILockoutUntilDateFactory)
    : ILockoutManager {

    private val lockoutThreshold = 5

    override val currentState: LockoutState
        get() {
            val failedAttempts = localStorage.failedAttempts
            val attemptsLeft = failedAttempts?.let {
                if (it >= lockoutThreshold){
                    val lockoutUptime = localStorage.lockoutUptime ?: uptimeProvider.uptime
                    lockoutUntilDateFactory.lockoutUntilDate(it, lockoutUptime,uptimeProvider.uptime)?.run {
                        return LockoutState.Locked(this)
                    }
                }
                val attempts = lockoutThreshold - it
                if (attempts < 1) 1 else attempts
            }
            return LockoutState.Unlocked(attemptsLeft != null)
        }

    override fun didFailUnlock() {
        val attempts = (localStorage.failedAttempts ?: 0) + 1
        if (attempts >= lockoutThreshold) {
            localStorage.lockoutUptime = uptimeProvider.uptime
        }
        localStorage.failedAttempts = attempts
    }

    override fun dropFailedAttempts() {
        localStorage.failedAttempts = null
    }


}