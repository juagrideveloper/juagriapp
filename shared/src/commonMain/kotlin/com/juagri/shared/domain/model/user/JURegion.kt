package com.juagri.shared.domain.model.user

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JURegion(
    @SerialName("cncode") var cnCode: String? = null,
    @SerialName("cnname") var cnName: String? = null,
    @SerialName("zcode") var zCode: String? = null,
    @SerialName("zname") var zName: String? = null,
    @SerialName("stcode") var stCode: String? = null,
    @SerialName("stname") var stName: String? = null,
    @SerialName("regcode") var regCode: String? = null,
    @SerialName("regname") var regName: String? = null,
    @SerialName("rmcode") var rmCode: String? = null,
    @SerialName("rmname") var rmName: String? = null,
    @SerialName("rmmailid") var rmMailId: String? = null,
    @SerialName("rmphoneno") var rmPhoneNo: String? = null,
    @SerialName("status") var status: Double? = 0.0,
    @SerialName("updatedTime") val updatedTime: Timestamp? = null
)