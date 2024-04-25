package com.juagri.shared.ui.promotion

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.filter.FilterType
import com.juagri.shared.domain.model.promotion.DistrictItem
import com.juagri.shared.domain.model.promotion.PromotionEventItem
import com.juagri.shared.domain.model.promotion.PromotionField
import com.juagri.shared.domain.model.promotion.VillageItem
import com.juagri.shared.domain.usecase.PromotionUseCase
import com.juagri.shared.domain.usecase.UserDetailsUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PromotionEntryViewModel(
    dataManager: DataManager,
    session: SessionPreference,
    private val promotionUseCase: PromotionUseCase
) : BaseViewModel(session, dataManager) {

    private var _promotionFieldItems: MutableStateFlow<UIState<List<PromotionField>>> =
        MutableStateFlow(UIState.Init)
    val promotionFieldItems = _promotionFieldItems.asStateFlow()

    var selectedPromotionEvent: MutableState<PromotionEventItem?> = mutableStateOf(null)
    var dropdownItem: MutableState<String> = mutableStateOf(names().select)
    val selectedDistrictItem: MutableState<DistrictItem> = mutableStateOf(DistrictItem())
    val selectedVillageItem: MutableState<VillageItem> = mutableStateOf(VillageItem())

    private var _setPromotionEntryData: MutableStateFlow<UIState<Boolean>> =
        MutableStateFlow(UIState.Init)
    val setPromotionEntryData = _setPromotionEntryData.asStateFlow()


    fun resetFields(){
        _promotionFieldItems.value = UIState.Init
        _setPromotionEntryData.value = UIState.Init
    }

    fun getPromotionEventList(){
        backgroundScope {
            promotionUseCase.getPromotionEventList().collect{response->
                uiScopeFilter(response, FilterType.PROMOTION_EVENT(PromotionEventItem()))
            }
        }
    }

    fun getDistrictList(){
        backgroundScope {
            promotionUseCase.getDistrictList(getJUEmployee()?.stateCode.value()).collect{response->
                uiScopeFilter(response,FilterType.DISTRICT(DistrictItem()))
            }
        }
    }
    fun getVillageList(){
        selectedDistrictItem.value.id?.let {
            backgroundScope {
                promotionUseCase.getVillageList(districtId = it).collect{response->
                    uiScopeFilter(response,FilterType.VILLAGE(VillageItem()))
                }
            }
        } ?: showErrorMessage(names().pleaseSelectValidRegion)
    }

    fun getPromotionFieldList(actId: String){
        backgroundScope {
            promotionUseCase.getPromotionEventFieldsList(actId).collect{response->
                uiScope(response,_promotionFieldItems)
            }
        }
    }

    fun setPromotionEntry(entryItems: Map<String,Any>, files: List<ByteArray>){
        backgroundScope {
            promotionUseCase.setPromotionEntry(entryItems, files).collect{response->
                uiScope(response,_setPromotionEntryData)
            }
        }
    }
}