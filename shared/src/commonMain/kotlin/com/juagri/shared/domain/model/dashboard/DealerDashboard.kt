package com.juagri.shared.domain.model.dashboard

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DealerDashboard(
    @SerialName("ccode") val cCode: String? = null,
    @SerialName("cd_available") val cdAvailable: Double = 0.0,
    @SerialName("cd_availed") val cdAvailed: Double = 0.0,
    @SerialName("cd_availmonth") val cdAvailMonth: String? = null,
    @SerialName("cdoid") val cdoId: String? = null,
    @SerialName("cname") val cName: String? = null,
    @SerialName("cphone") val cPhone: String? = null,
    @SerialName("g180") val g180: Double = 0.0,
    @SerialName("l120") val l120: Double = 0.0,
    @SerialName("l180") val l180: Double = 0.0,
    @SerialName("l90") val l90: Double = 0.0,
    @SerialName("msal_value") val msalValue: Double = 0.0,
    @SerialName("regcode") val regCode: String? = null,
    @SerialName("tcode") val tCode: String? = null,
    @SerialName("today") val today: String? = null,
    @SerialName("totalos") val totalOS: Double = 0.0,
    @SerialName("ysal_value") val ysalValue: Double = 0.0,
    @SerialName("updatedTime") val updatedTime: Timestamp? = null
)

@Serializable
data class DealerSales(
    @SerialName("id")
    val id: String? = null,
    @SerialName("ccode")
    val cCode: String? = null,
    @SerialName("bcode")
    val bcode: String? = null,
    @SerialName("brand")
    val brand: String? = null,
    @SerialName("qty")
    val qty: Double = 0.0,
    @SerialName("type")
    val type: String? = null,
    @SerialName("status")
    val status: Double? = 0.0,
    @SerialName("updatedTime")
    val updatedTime: Timestamp? = null
)