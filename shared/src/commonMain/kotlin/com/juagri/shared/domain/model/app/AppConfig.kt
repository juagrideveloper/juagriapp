package com.juagri.shared.domain.model.app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(
    @SerialName("version_code")
    val versionCode: Int = 0,
    @SerialName("version_name")
    val versionName: String = ""
)