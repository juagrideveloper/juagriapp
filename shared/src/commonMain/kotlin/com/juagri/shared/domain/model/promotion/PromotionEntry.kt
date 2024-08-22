package com.juagri.shared.domain.model.promotion

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromotionEntry(
    @SerialName("activity_code")
    var actId: String? = null,
    @SerialName("updated_emprole")
    var roleId: String? = null,
    @SerialName("updated_time")
    var updatedTime: Timestamp? = null
)