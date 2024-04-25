package com.juagri.shared.domain.model.promotion

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromotionField(
    @SerialName("actid")
    var actId: String? = null,
    @SerialName("data")
    var data: String? = null,
    @SerialName("editable")
    var editable: Boolean = false,
    @SerialName("fieldid")
    var fieldId: String? = null,
    @SerialName("ismandatory")
    var isMandatory: Boolean = false,
    @SerialName("length")
    var length: Double? = null,
    @SerialName("maxlength")
    var maxlength: Double? = null,
    @SerialName("name")
    var name: String? = null,
    @SerialName("param")
    var param: String? = null,
    @SerialName("parentid")
    var parentId: Double? = null,
    @SerialName("slno")
    var slNo: Double? = null,
    @SerialName("type")
    var type: Double? = null,
    @SerialName("validation")
    var validation: Double? = null,
    @SerialName("value")
    var value: String? = null,
    @SerialName("status")
    var status: Double? = null,
    @SerialName("updatedTime")
    var updatedTime: Timestamp? = null,
    var valueItem: MutableState<PromotionValue>? = null,
    val selectValue: MutableState<String> = mutableStateOf("")
)

@Serializable
sealed class PromotionValue{
    data class AreaDropdown(val data: MutableState<AreaDropdownItem>? = null) : PromotionValue()
    data class Label(val data: MutableState<String>? = null) : PromotionValue()
    data class Text(val data: MutableState<String>? = null) : PromotionValue()
    data class Email(val data: MutableState<String>? = null) : PromotionValue()
    data class Number(val data: MutableState<String>? = null) : PromotionValue()

}