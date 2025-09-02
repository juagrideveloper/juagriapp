package com.juagri.shared.domain.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JURole(
    @SerialName("roleId") var roleId: String? = null,
    @SerialName("roleName") var roleName: String? = null
)