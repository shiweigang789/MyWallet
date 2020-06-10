package com.topnetwork.wallet.entities

import com.topnetwork.wallet.core.FeeRatePriority

data class FeeRateInfo(val priority: FeeRatePriority, val feeRate: Long, val duration: Long)
