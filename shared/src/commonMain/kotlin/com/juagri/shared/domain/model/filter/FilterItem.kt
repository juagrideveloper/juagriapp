package com.juagri.shared.domain.model.filter

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.juagri.shared.domain.model.promotion.DistrictItem
import com.juagri.shared.domain.model.promotion.PromotionEventItem
import com.juagri.shared.domain.model.promotion.VillageItem
import com.juagri.shared.domain.model.user.FinMonth
import com.juagri.shared.domain.model.user.FinYear
import com.juagri.shared.domain.model.user.JUDealer
import com.juagri.shared.domain.model.user.JURegion
import com.juagri.shared.domain.model.user.JUTerritory

data class FilterItem(var code: String,var name: String,  var data: FilterType)

data class FilterDataItem(
    val title: String = "",
    val filterItems: List<FilterItem> = listOf(),
    var isEnabled: MutableState<Boolean> = mutableStateOf(false)
)

sealed class FilterType {
    data class REGION(val data: JURegion) : FilterType()
    data class TERRITORY(val data: JUTerritory) : FilterType()
    data class DEALER(val data: JUDealer) : FilterType()
    data class FIN_YEAR(val data: FinYear) : FilterType()
    data class FIN_MONTH(val data: FinMonth) : FilterType()
    data class PROMOTION_EVENT(val data: PromotionEventItem) : FilterType()
    data class DISTRICT(val data: DistrictItem) : FilterType()
    data class VILLAGE(val data: VillageItem) : FilterType()
}