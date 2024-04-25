package com.juagri.shared.ui.ledger

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.filter.FilterType
import com.juagri.shared.domain.model.ledger.DealerLedgerItem
import com.juagri.shared.domain.model.user.FinMonth
import com.juagri.shared.domain.model.user.FinYear
import com.juagri.shared.domain.model.user.JUDealer
import com.juagri.shared.domain.model.user.JURegion
import com.juagri.shared.domain.model.user.JUTerritory
import com.juagri.shared.domain.usecase.DealerLedgerUseCase
import com.juagri.shared.domain.usecase.UserDetailsUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.isNotEqualTo
import com.juagri.shared.utils.selectedValue
import com.juagri.shared.utils.value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LedgerViewModel(
    session: SessionPreference,
    dataManager: DataManager,
    private val userDetailsUseCase: UserDetailsUseCase,
    private val ledgerUseCase: DealerLedgerUseCase
):BaseViewModel(session,dataManager) {

    private var _dealerLedgerItem: MutableStateFlow<UIState<DealerLedgerItem?>> =
        MutableStateFlow(UIState.Init)
    val dealerLedgerItem = _dealerLedgerItem.asStateFlow()



    fun getRegionList(){
        backgroundScope{
            userDetailsUseCase.getRegionList().collect{ response ->
                uiScopeFilter(response, FilterType.REGION(JURegion()))
            }
        }
    }

    fun getTerritoryList(){
        selectedRegion.value?.let {
            backgroundScope {
                userDetailsUseCase.getTerritoryList(it.regCode.value()).collect { response ->
                    writeLog(it.regCode.value())
                    uiScopeFilter(response, FilterType.TERRITORY(JUTerritory()))
                }
            }
        }?: showErrorMessage(names().pleaseSelectValidRegion)
    }

    fun getDealerList(){
        selectedTerritory.value?.let {
            backgroundScope {
                userDetailsUseCase.getDealerList(it.tCode.value()).collect { response ->
                    uiScopeFilter(response, FilterType.DEALER(JUDealer()))
                }
            }
        }?: showErrorMessage(names().pleaseSelectValidTerritory)
    }

    fun getFinYearList(){
        backgroundScope{
            userDetailsUseCase.getFinYear().collect{ response ->
                uiScopeFilter(response, FilterType.FIN_YEAR(FinYear()))
            }
        }
    }

    fun getFinMonthList(){
        selectedFinYear.value?.let {
            backgroundScope {
                userDetailsUseCase.getFinMonth(it.startDate!!,it.endDate!!).collect { response ->
                    uiScopeFilter(response, FilterType.FIN_MONTH(FinMonth()))
                }
            }
        }?: showErrorMessage(names().pleaseSelectValidFinYear)
    }

    fun getLedgerDetails(){
        selectedRegion.value?.let {
            selectedTerritory.value?.let {
                selectedDealer.value?.let {dealer->
                    selectedFinYear.value?.let {finYear->
                        selectedFinMonth.value?.let {finMonth->
                            var start = finYear.startDate!!
                            var end = finYear.endDate!!
                            if(finMonth.fMonth.isNotEqualTo(names().all)){
                                start = finMonth.startDate!!
                                end = finMonth.endDate!!
                            }
                            backgroundScope {
                                ledgerUseCase.getDealerLedgerItem(dealer.cCode.value(),start,end).collect { response ->
                                    uiScope(response,_dealerLedgerItem)
                                }
                            }
                        }?: showErrorMessage(names().pleaseSelectValidFinMonth)
                    }?: showErrorMessage(names().pleaseSelectValidFinYear)
                }?: showErrorMessage(names().pleaseSelectValidDealer)
            }   ?: showErrorMessage(names().pleaseSelectValidTerritory)
        }?: showErrorMessage(names().pleaseSelectValidRegion)
    }

    fun resetLedger() {
        _dealerLedgerItem.value = UIState.Success(null)
    }
}