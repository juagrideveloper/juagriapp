package com.juagri.shared.domain.model.user

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FinYear(
    @SerialName("fyear") val fYear: String? = null,
    @SerialName("start_date") val startDate: Timestamp? = null,
    @SerialName("end_date") val endDate: Timestamp? = null,
    @SerialName("status") var status: Double? = 0.0,
    @SerialName("updatedTime") val updatedTime: Timestamp? = null
)