package com.topnetwork.wallet.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import com.topnetwork.wallet.core.storage.AccountRecord

@Entity(primaryKeys = ["coinId", "accountId"],
        foreignKeys = [ForeignKey(
                entity = AccountRecord::class,
                parentColumns = ["id"],
                childColumns = ["accountId"],
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE,
                deferred = true)
        ])

data class EnabledWallet(
        val coinId: String,
        val accountId: String,
        val walletOrder: Int? = null
)
