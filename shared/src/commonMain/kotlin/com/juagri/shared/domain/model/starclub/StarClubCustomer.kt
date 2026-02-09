package com.juagri.shared.domain.model.starclub

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Firestore Star Club document per customer (document id = ccode).
 * metrics keys: TotalSales, FocusProduct, DSO, PayDec25, PayJan26, PayJun26
 */
@Serializable
data class StarClubCustomer(
    @SerialName("ccode") val ccode: String? = null,
    @SerialName("cname") val cname: String? = null,
    @SerialName("achievedClub") val achievedClub: String? = null,
    @SerialName("clubs") val clubs: Map<String, Map<String, StarClubMetric>>? = null,
    // Legacy fallback (older schema)
    @SerialName("metrics") val metrics: Map<String, StarClubMetric>? = null
)

@Serializable
data class StarClubMetric(
    @SerialName("achieved") val achieved: Double = 0.0,
    @SerialName("shortfall") val shortfall: Double = 0.0,
    @SerialName("target") val target: Double = 0.0
)
