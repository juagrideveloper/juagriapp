package com.juagri.shared.domain.model.user

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JUTerritory(
    @SerialName("socode") var soCode: String? = null,
    @SerialName("soname") var soName: String? = null,
    @SerialName("somailid") var soMailId: String? = null,
    @SerialName("sophoneno") var soPhoneNo: String? = null,
    @SerialName("tcode") var tCode: String? = null,
    @SerialName("tname") var tName: String? = null,
    @SerialName("regcode") var regCode: String? = null,
    @SerialName("status") var status: Double? = 0.0,
    @SerialName("updatedTime") val updatedTime: Timestamp? = null
)