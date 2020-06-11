package com.topnetwork.wallet.core

/**
 * @FileName     : Extensions
 * @date         : 2020/6/11 11:18
 * @author       : Owen
 * @description  :
 */

// String

fun String.hexToByteArray(): ByteArray {
    return ByteArray(this.length / 2) {
        this.substring(it * 2, it * 2 + 2).toInt(16).toByte()
    }
}

// ByteArray

fun ByteArray.toHexString(): String {
    return this.joinToString(separator = "") {
        it.toInt().and(0xff).toString(16).padStart(2, '0')
    }
}