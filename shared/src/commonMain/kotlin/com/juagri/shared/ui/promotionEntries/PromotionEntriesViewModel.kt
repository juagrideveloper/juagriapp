package com.juagri.shared.ui.promotionEntries

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.filter.FilterType
import com.juagri.shared.domain.model.promotion.ParticipateDialogData
import com.juagri.shared.domain.model.promotion.ParticipationEntry
import com.juagri.shared.domain.model.user.JURegion
import com.juagri.shared.domain.model.user.JUTerritory
import com.juagri.shared.domain.usecase.PromotionEntriesUseCase
import com.juagri.shared.domain.usecase.UserDetailsUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PromotionEntriesViewModel(
    dataManager: DataManager,
    session: SessionPreference,
    private val userDetailsUseCase: UserDetailsUseCase,
    private val promotionEntriesUseCase: PromotionEntriesUseCase
) : BaseViewModel(session, dataManager) {

    private var _promotionEntries: MutableStateFlow<UIState<List<Map<String,Any>>>> =
        MutableStateFlow(UIState.Init)
    val promotionEntries = _promotionEntries.asStateFlow()

    private var _participationEntry: MutableStateFlow<UIState<String>> =
        MutableStateFlow(UIState.Init)
    val participationEntry = _participationEntry.asStateFlow()

    val enableRegionFilter: MutableState<Boolean> = mutableStateOf(false)
    val enableTerritoryFilter: MutableState<Boolean> = mutableStateOf(false)

    fun getRegionList(){
        backgroundScope{
            uiScopeFilter(
                ResponseState.Success(getJUEmployee()?.regionList),
                FilterType.REGION(JURegion())
            )
        }
    }

    fun getTerritoryList(){
        selectedRegion.value?.let {region->
            backgroundScope {
                uiScopeFilter(
                    ResponseState.Success(getJUEmployee()?.territoryList?.filter { it.regCode == region.regCode }),
                    FilterType.TERRITORY(JUTerritory()),
                    getRoleID() != "SO"
                )
            }
            /*backgroundScope {
                userDetailsUseCase.getTerritoryList(it.regCode.value()).collect { response ->
                    writeLog(it.regCode.value())
                    uiScopeFilter(response, FilterType.TERRITORY(JUTerritory()), true)
                }
            }*/
        }?: showErrorMessage(names().pleaseSelectValidRegion)
    }

    fun resetParticipationEntry() {
        _participationEntry.value = UIState.Init
        showParticipateDialog.value = ParticipateDialogData()
    }

    fun getPromotionEntries(code: String? = null){
        var selectCode: String? = null
        code?.let {
            selectCode = if(it == names().all){
                selectedRegion.value?.regCode
            }else{
                code
            }
        }?: run {
            getJUEmployee()?.let {
                if(it.territoryList.isNotEmpty()) {
                    if (it.territoryList.size > 1) {
                        enableTerritoryFilter.value = true
                    } else {
                        selectCode = it.territoryList[0].tCode
                    }
                    if(it.regionList.size > 1) {
                        enableRegionFilter.value = true
                    }else{
                        selectedRegion.value = it.regionList.first()
                    }
                }
            }
        }
        selectCode?.let {
            backgroundScope {
                promotionEntriesUseCase.getRecentPromotionEntries(it, getRoleID()).collect{response->
                    uiScope(response, _promotionEntries)
                }
            }
        }
    }

    fun setParticipationEntry(entry: ParticipationEntry, updatedDetails: Map<String, Any>){
        backgroundScope {
            promotionEntriesUseCase.setPromotionParticipation(entry, updatedDetails, getRoleID()).collect{response->
                uiScope(response, _participationEntry)
            }
        }
    }
}