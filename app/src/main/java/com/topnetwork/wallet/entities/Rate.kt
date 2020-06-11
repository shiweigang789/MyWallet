package com.topnetwork.wallet.entities

import androidx.room.Entity
import androidx.room.TypeConverters
import com.topnetwork.wallet.core.storage.DatabaseConverters
import java.math.BigDecimal
import java.util.*

/**
 * @FileName     : Rate
 * @date         : 2020/6/11 9:56
 * @author       : Owen
 * @description  :
 */
@Entity(primaryKeys = ["coinCode", "currencyCode", "timestamp", "isLatest"])
@TypeConverters(DatabaseConverters::class)
data class Rate(
    var coinCode: String,
    var currencyCode: String,
    var value: BigDecimal,
    var timestamp: Long,
    var isLatest: Boolean
) {

    val expired: Boolean
        get() {
            val diff = (Date().time / 1000) - timestamp
            return diff > 60 * 10
        }
}