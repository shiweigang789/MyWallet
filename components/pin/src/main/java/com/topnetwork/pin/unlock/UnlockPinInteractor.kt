package com.topnetwork.pin.unlock

import androidx.biometric.BiometricPrompt
import com.topnetwork.core.IEncryptionManager
import com.topnetwork.core.IPinComponent
import com.topnetwork.core.ISystemInfoManager
import com.topnetwork.pin.core.ILockoutManager
import com.topnetwork.pin.core.LockoutState
import com.topnetwork.pin.core.OneTimeTimer
import com.topnetwork.pin.core.OneTimerDelegate

/**
 * @FileName     : UnlockPinInteractor
 * @date         : 2020/6/10 11:49
 * @author       : Owen
 * @description  :
 */
class UnlockPinInteractor(
    private val pinComponent: IPinComponent,
    private val lockoutManager: ILockoutManager,
    private val encryptionManager: IEncryptionManager,
    private val systemInfoManager: ISystemInfoManager,
    private val timer: OneTimeTimer
) : UnlockPinModule.IInteractor, OneTimerDelegate {

    var delegate: UnlockPinModule.IInteractorDelegate? = null

    init {
        timer.delegate = this
    }

    override val isFingerprintEnabled: Boolean
        get() = pinComponent.isFingerprintEnabled
    override val biometricAuthSupported: Boolean
        get() = systemInfoManager.biometricAuthSupported
    override val cryptoObject: BiometricPrompt.CryptoObject?
        get() = encryptionManager.getCryptoObject()

    override fun unlock(pin: String): Boolean {
        val valid = pinComponent.validate(pin)
        if (valid) {
            pinComponent.onUnlock()
            lockoutManager.dropFailedAttempts()
        } else {
            lockoutManager.didFailUnlock()
            updateLockoutState()
        }

        return valid
    }

    override fun onUnlock() {
        delegate?.unlock()
        pinComponent.onUnlock()
    }

    override fun onFire() {
        updateLockoutState()
    }

    override fun updateLockoutState() {
        val state = lockoutManager.currentState
        when (state) {
            is LockoutState.Locked -> timer.schedule(state.until)
        }

        delegate?.updateLockoutState(state)
    }
}