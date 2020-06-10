package com.topnetwork.wallet.entities

import android.os.Parcelable
import com.topnetwork.wallet.R
import kotlinx.android.parcel.Parcelize

/**
 * @FileName     : PredefinedAccountType
 * @date         : 2020/6/10 15:43
 * @author       : Owen
 * @description  :
 */
sealed class PredefinedAccountType: Parcelable {
    @Parcelize
    object Standard : PredefinedAccountType()
    @Parcelize
    object Eos : PredefinedAccountType()
    @Parcelize
    object Binance : PredefinedAccountType()

    val title: Int
        get() = when (this) {
            is Standard -> R.string.AccountType_Unstoppable
            is Eos -> R.string.AccountType_Eos
            is Binance -> R.string.AccountType_Binance
        }

    val coinCodes: Int
        get() = when (this) {
            is Standard -> R.string.AccountType_Unstoppable_Text
            is Eos -> R.string.AccountType_Eos_Text
            is Binance -> R.string.AccountType_Binance_Text
        }

    fun supports(accountType: AccountType): Boolean {
        when (this) {
            is Standard -> {
                if (accountType is AccountType.Mnemonic) {
                    return accountType.words.size == 12
                }
            }
            is Eos -> {
                return accountType is AccountType.Eos
            }
            is Binance -> {
                if (accountType is AccountType.Mnemonic) {
                    return accountType.words.size == 24
                }
            }
        }

        return false
    }

    fun isCreationSupported(): Boolean = when (this) {
        is Standard, is Binance -> true
        is Eos -> false
    }

    override fun toString(): String {
        return when (this) {
            is Standard -> STANDARD
            is Eos -> EOS
            is Binance -> BINANCE
        }
    }

    companion object{
        const val STANDARD = "standard"
        const val EOS = "eos"
        const val BINANCE = "binance"

        fun fromString(string: String?): PredefinedAccountType? {
            return when (string) {
                STANDARD -> Standard
                EOS -> Eos
                BINANCE -> Binance
                else -> null
            }
        }
    }

}