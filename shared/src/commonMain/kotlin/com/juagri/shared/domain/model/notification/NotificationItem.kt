package com.juagri.shared.domain.model.notification

import com.juagri.shared.utils.Constants
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationItem(
    @SerialName("id")
    var id: String = "",
    @SerialName(Constants.FIELD_REG_CODE)
    val regCode: String = "",
    @SerialName("regName")
    val regName: String = "",
    @SerialName(Constants.FIELD_ROLE_ID)
    val roleId: String = "",
    @SerialName("roleName")
    val roleName: String = "",
    @SerialName("title")
    val title: String = "",
    @SerialName("content")
    val content: String = "",
    @SerialName("filename")
    var filename: String = "",
    @SerialName("unread")
    var unread: Int = 0,
    @SerialName(Constants.FIELD_UPDATED_TIME)
    var updatedTime: Timestamp? = null
)
