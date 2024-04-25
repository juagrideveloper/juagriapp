package com.juagri.shared.domain.model.promotion


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromotionParentItem(
    @SerialName("Data")
    val child: List<PromotionChildItem>,
    @SerialName("Name")
    val name: String
)

@Serializable
data class PromotionChildItem(
    @SerialName("Name")
    val name: String,
    var isSelected: MutableState<Boolean> = mutableStateOf(false)
)

data class PromotionFilterDataItem(
    val title: String = "",
    val parentFilterItems: List<PromotionParentItem> = listOf(),
    val childFilterItems: List<PromotionChildItem> = listOf(),
    var selectedItems: MutableState<String> = mutableStateOf(""),
    var isEnabled: MutableState<Boolean> = mutableStateOf(false)
)