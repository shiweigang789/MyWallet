package com.topnetwork.core.security

import androidx.biometric.BiometricPrompt
import com.topnetwork.core.IEncryptionManager
import com.topnetwork.core.IKeyProvider
import javax.crypto.Cipher

/**
 * @FileName     : EncryptionManager
 * @date         : 2020/6/8 15:45
 * @author       : Owen
 * @description  :
 */
class EncryptionManager(private val keyProvider: IKeyProvider) : IEncryptionManager {

    @Synchronized
    override fun encrypt(data: String): String {
        return CipherWrapper().encrypt(data, keyProvider.getKey())
    }

    @Synchronized
    override fun decrypt(data: String): String {
        return CipherWrapper().decrypt(data, keyProvider.getKey())
    }

    override fun getCryptoObject(): BiometricPrompt.CryptoObject? {
        val cipher = CipherWrapper().cipher
        cipher.init(Cipher.ENCRYPT_MODE, keyProvider.getKey())
        return BiometricPrompt.CryptoObject(cipher)
    }

}