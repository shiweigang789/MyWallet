package com.topnetwork.pin.core

import android.text.TextUtils
import com.topnetwork.core.CoreApp
import com.topnetwork.core.IEncryptionManager
import com.topnetwork.core.ISecuredStorage

/**
 * @FileName     : SecureStorage
 * @date         : 2020/6/10 9:38
 * @author       : Owen
 * @description  :
 */
class SecureStorage(private val encryptionManager: IEncryptionManager) : ISecuredStorage {

    private val lockPin = "lock_pin"
    private val fingerprintEnabled = "fingerprint_enabled"

    override var isFingerprintEnabled: Boolean
        get() = CoreApp.preferences.getBoolean(fingerprintEnabled, false)
        set(enabled) {
            CoreApp.preferences.edit().putBoolean(fingerprintEnabled, enabled).apply()
        }

    override val savedPin: String?
        get() {
            val string = CoreApp.preferences.getString(lockPin, null) ?: return null
            return if (TextUtils.isEmpty(string)) {
                null
            } else {
                encryptionManager.decrypt(string)
            }
        }

    override fun savePin(pin: String) {
        CoreApp.preferences.edit().putString(lockPin, encryptionManager.encrypt(pin)).apply()
    }

    override fun removePin() {
        CoreApp.preferences.edit().remove(lockPin).apply()
    }

    override fun pinIsEmpty(): Boolean {
        val string = CoreApp.preferences.getString(lockPin, null)
        return string.isNullOrEmpty()
    }
}