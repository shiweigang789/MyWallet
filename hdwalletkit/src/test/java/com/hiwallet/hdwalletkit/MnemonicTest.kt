package com.hiwallet.hdwalletkit

import android.os.PowerManager
import org.bitcoinj.crypto.MnemonicCode
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PowerMockIgnore

import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.security.SecureRandom

/**
 * @FileName     : MnemonicTest
 * @date         : 2020/8/19 18:32
 * @author       : Owen
 * @description  : 助记词测试
 */

@RunWith(PowerMockRunner::class)
@PrepareForTest(SecureRandom::class, Mnemonic::class, MnemonicCode::class)
@PowerMockIgnore("javax.crypto.*")

class MnemonicTest {

    private val mnemonic = Mnemonic()

    @Test
    fun toMnemonic_Success() {
        val entropy: ByteArray = hexStringToByteArray("7787bfe5815e1912a1ec409a56391106")
        val mnemonicWords = mnemonic.toMnemonic(entropy).joinToString(separator = " ")
        val expectedWords =
            "jealous digital west actor thunder matter marble marine olympic range dust banner"
        Assert.assertEquals(mnemonicWords, expectedWords)
    }

    @Test(expected = Mnemonic.EmptyEntropyException::class)
    @Throws(Exception::class)
    fun toMnemonic_EmptyEntropy() {
        val entropy: ByteArray = hexStringToByteArray("")
        mnemonic.toMnemonic(entropy)
    }

    @Test
    fun validate_Success() {

        val mnemonicKeys = listOf("jealous", "digital", "west", "actor", "thunder", "matter", "marble", "marine", "olympic", "range", "dust", "banner")

        mnemonic.validate(mnemonicKeys)
    }

    @Test(expected = Mnemonic.InvalidMnemonicCountException::class)
    fun validate_WrongWordsCount() {

        val mnemonicKeys = listOf("digital", "west", "actor", "thunder", "matter", "marble", "marine", "olympic", "range", "dust", "banner")

        mnemonic.validate(mnemonicKeys)
    }


    @Test(expected = Mnemonic.InvalidMnemonicKeyException::class)
    fun validate_InvalidMnemonicKey() {

        val mnemonicKeys = listOf("jealous", "digitalll", "west", "actor", "thunder", "matter", "marble", "marine", "olympic", "range", "dust", "banner")

        mnemonic.validate(mnemonicKeys)
    }

    @Test
    fun toSeed_Success() {

        val mnemonicKeys = listOf("jealous", "digital", "west", "actor", "thunder", "matter", "marble", "marine", "olympic", "range", "dust", "banner")

        val seed = mnemonic.toSeed(mnemonicKeys)

        val expectedSeed = hexStringToByteArray("6908630f564bd3ca9efb521e72da86727fc78285b15decedb44f40b02474502ed6844958b29465246a618b1b56b4bdffacd1de8b324159e0f7f594c611b0519d")

        Assert.assertArrayEquals(seed, expectedSeed)
    }

    @Test
    fun toSeed_WrongWordsCount() {

        val mnemonicKeys = listOf("digital", "west", "actor", "thunder", "matter", "marble", "marine", "olympic", "range", "dust", "banner")
//
        mnemonic.toSeed(mnemonicKeys)
    }

    @Test
    fun toSeed_InvalidMnemonicKey() {
// tiger purpose increase trash detect search hold wealth moon exhaust garden banner
//        val mnemonicKeys = listOf("jealous", "digitalll", "west", "actor", "thunder", "matter", "marble", "marine", "olympic", "range", "dust", "banner")
        val mnemonicKeys = listOf("tiger", "purpose", "increase", "trash", "detect", "search", "hold", "wealth", "moon", "exhaust", "garden", "banner")
//        mnemonic.toSeed(mnemonicKeys)
        MnemonicCode.toSeed(mnemonicKeys, "")
//        mnemonic.validate(mnemonicKeys)
    }

    private fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)

        var i = 0
        while (i < len) {
            data[i / 2] =
                ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }

        return data
    }


}