package com.juagri.shared.domain.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OTPResponse(
    @SerialName("isSent")
    val isSent: Boolean,
    @SerialName("otp")
    val otp: String
)