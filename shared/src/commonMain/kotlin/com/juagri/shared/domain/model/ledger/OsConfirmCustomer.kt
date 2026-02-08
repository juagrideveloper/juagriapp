package com.juagri.shared.domain.model.ledger

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OsConfirmCustomer(
    @SerialName("ccode")
    val ccode: String = "",
    @SerialName("cname")
    val cname: String = "",
    @SerialName("cphone")
    val cphone: String = "",
    @SerialName("emailcc")
    val emailcc: String = "",
    @SerialName("emailto")
    val emailto: String = "",
    @SerialName("osdate")
    val osdate: String = "",
    @SerialName("osmonth")
    val osmonth: String = "",
    @SerialName("range")
    val range: String = "",
    @SerialName("regcode")
    val regcode: String = "",
    @SerialName("tcode")
    val tcode: String = "",
    @SerialName("totalos")
    val totalos: Double = 0.0
)
