package com.juagri.shared.domain.model.user

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JUDealer(
    @SerialName("ccode") var cCode: String? = null,
    @SerialName("cname") var cName: String? = null,
    @SerialName("mailId") var mailId: String? = null,
    @SerialName("address") var address: String? = null,
    @SerialName("phoneno") var phoneNo: String? = null,
    @SerialName("tcode") var tCode: String? = null,
    @SerialName("regcode") var regCode: String? = null,
    @SerialName("status") var status: Double? = 0.0,
    @SerialName("updatedTime") val updatedTime: Timestamp? = null
)