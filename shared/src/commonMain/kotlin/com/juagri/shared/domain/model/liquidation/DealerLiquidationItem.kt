package com.juagri.shared.domain.model.liquidation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class DealerLiquidationData(
    val config: DealerLiquidationConfig,
    val liquidationItems: List<DealerLiquidationItem>
)

@Serializable
data class DealerLiquidationItem(
    @SerialName("ccode")
    val cCode: String,
    @SerialName("cname")
    val cName: String,
    @SerialName("regcode")
    val regCode: String,
    @SerialName("status")
    val status: Double,
    @SerialName("tcode")
    val tCode: String,
    @SerialName("branditems")
    val brandItems: List<LiquidationItem>,
    @SerialName("updatedTime")
    val updatedTime: Timestamp? = null
)

@Serializable
data class LiquidationItem(
    @SerialName("bcode")
    val bCode: String,
    @SerialName("bname")
    val bName: String,
    @SerialName("pqty")
    val pQty: Double,
    @SerialName("stock")
    var stock: Double,
    val stockItem: MutableState<Double> = mutableStateOf(stock)
)