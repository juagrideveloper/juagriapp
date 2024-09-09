package com.juagri.shared.domain.model.focusProduct

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class CDOFocusProductItem(
    @SerialName("cdoid")
    var cdoId: String? = null,
    @SerialName("focusproducts")
    var focusProducts: List<FocusProductItem> = listOf(),
    @SerialName("regcode")
    var regCode: String? = null,
    @SerialName("tcode")
    var tCode: String? = null
)

@Serializable
data class FocusProductItem(
    @SerialName("bcode")
    var code: String? = null,
    @SerialName("bname")
    var name: String? = null,
    @SerialName("monplan")
    var mPlan: Double? = 0.0,
    @SerialName("monact")
    var mActual: Double? = 0.0,
    @SerialName("yrplan")
    var yPlan: Double? = 0.0,
    @SerialName("yract")
    var yActual: Double? = 0.0
)