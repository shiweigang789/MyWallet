package com.topnetwork.pin.core

import android.security.keystore.UserNotAuthenticatedException
import com.topnetwork.core.ISecuredStorage

/**
 * @FileName     : PinManager
 * @date         : 2020/6/10 9:26
 * @author       : Owen
 * @description  :
 */
class PinManager(private val securedStorage: ISecuredStorage) {

    val isPinSet: Boolean
        get() = !securedStorage.pinIsEmpty()

    var isFingerprintEnabled: Boolean
        get() = securedStorage.isFingerprintEnabled
        set(value) {
            securedStorage.isFingerprintEnabled = value
        }

    @Throws(UserNotAuthenticatedException::class)
    fun store(pin: String) {
        securedStorage.savePin(pin)
    }

    fun validate(pin: String): Boolean {
        return securedStorage.savedPin == pin
    }

    fun clear() {
        securedStorage.removePin()
        securedStorage.isFingerprintEnabled = false
    }

}

