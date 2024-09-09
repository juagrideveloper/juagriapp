package com.juagri.shared.domain.model.user

import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginInfo(
    @SerialName("emp_code")
    val empCode: String = "",
    @SerialName("emp_name")
    val empName: String = "",
    @SerialName("device_mode")
    val deviceMode: String = "",
    @SerialName("device_os")
    val deviceOS: String = "",
    @SerialName("device_sdk")
    val deviceSDK: String = "",
    @SerialName("app_version")
    val appVersion: String = "",
    @SerialName("emp_role")
    val empRole: String = "",
    @SerialName("emp_role_id")
    val empRoleId: String = "",
    @SerialName(Constants.FIELD_T_CODE)
    val tCode: String = "",
    @SerialName(Constants.FIELD_REG_CODE)
    val regCode: String = "",
    @SerialName("last_online")
    val lastOnline: Timestamp = Timestamp.now()
)