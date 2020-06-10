package com.topnetwork.wallet.core.managers

import android.app.Activity
import android.app.KeyguardManager
import android.os.Build
import androidx.biometric.BiometricManager
import com.topnetwork.core.ISystemInfoManager
import com.topnetwork.wallet.BuildConfig
import com.topnetwork.wallet.core.App

/**
 * @FileName     : SystemInfoManager
 * @date         : 2020/6/10 18:45
 * @author       : Owen
 * @description  :
 */
class SystemInfoManager : ISystemInfoManager {

    private val biometricManager by lazy { BiometricManager.from(App.instance) }

    override val appVersion: String = BuildConfig.VERSION_NAME
    override val isSystemLockOff: Boolean
        get() {
            val keyguardManager = App.instance.getSystemService(Activity.KEYGUARD_SERVICE) as KeyguardManager
            return !keyguardManager.isDeviceSecure
        }
    override val biometricAuthSupported: Boolean
        get() = biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
    override val deviceModel: String
        get() =  "${Build.MANUFACTURER} ${Build.MODEL}"
    override val osVersion: String
        get() = "Android ${Build.VERSION.RELEASE} (${Build.VERSION.SDK_INT})"
}