package com.juagri.shared.domain.model.promotion

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromotionEventItem(
    @SerialName("id") var id: String? = null,
    @SerialName("name") var name: String? =null,
    @SerialName("order_by") var orderBy: Double? = null,
    @SerialName("status") var status: Double? = null,
    @SerialName("updatedTime") var updatedTime: Timestamp? = null
)
