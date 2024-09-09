package com.juagri.shared.domain.model.liquidation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DealerLiquidationConfig(
    @SerialName("cust_codes")
    val dealerCodes: String = "",
    @SerialName("is_enabled")
    val isEnabled: Boolean = false,
    @SerialName("week_no")
    val weekNo: Double = 0.0
)