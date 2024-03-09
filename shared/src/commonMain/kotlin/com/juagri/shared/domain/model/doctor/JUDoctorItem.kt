package com.juagri.shared.domain.model.doctor

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JUDoctorItem(
    @SerialName("name") var name: String? = null,
    @SerialName("image") var image: String? = null,
    @SerialName("type") var type: Int? = null,
    @SerialName("child") var child: List<JUDoctorItem> = listOf(),
    @SerialName("updatedTime") var updatedTime: Timestamp = Timestamp.now()
)

@Serializable
data class JUDoctorDataItem(
    @SerialName("id") var id: String? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("image") var image: String? = null,
    @SerialName("type") var type: Int? = null,
    @SerialName("parentId") var parentId: String? = null,
    @SerialName("has_child") var hasChild: Boolean = false,
    @SerialName("status") var status: Int? = 0,
    @SerialName("updatedTime") var updatedTime: Timestamp = Timestamp.now()
)