package com.juagri.shared.domain.model.ledger

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OsConfirmConfig(
    @SerialName("custids")
    val custIds: String = "",
    @SerialName("editable")
    val editable: Boolean = true,
    @SerialName("finperiod")
    val finPeriod: String = "",
    @SerialName("notes")
    val notes: String = "",
    @SerialName("regionids")
    val regionIds: String = "",
    @SerialName("territoryids")
    val territoryIds: String = ""
)
