package com.juagri.shared.ui.liquidation

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.liquidation.DealerLiquidationData
import com.juagri.shared.domain.usecase.DealerLiquidationUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LiquidationViewModel(
    dataManager: DataManager,
    session: SessionPreference,
    private val dealerLiquidationUseCase: DealerLiquidationUseCase
) : BaseViewModel(session, dataManager) {

    private var _dealerLiquidationItems: MutableStateFlow<UIState<DealerLiquidationData>> =
        MutableStateFlow(UIState.Init)
    val dealerLiquidationItems = _dealerLiquidationItems.asStateFlow()

    private var _updateLiquidationItems: MutableStateFlow<UIState<Boolean>> =
        MutableStateFlow(UIState.Init)
    val updateLiquidationItems = _updateLiquidationItems.asStateFlow()

    fun getDealerLiquidationItems(){
        _updateLiquidationItems.value = UIState.Init
        backgroundScope {
            dealerLiquidationUseCase.getDealerLiquidationItems(getJUEmployee()?.territoryCode.value()).collect{response->
                uiScope(response, _dealerLiquidationItems)
            }
        }
    }

    fun setUpdateLiquidation(data: DealerLiquidationData){
        getJUEmployee()?.let {
            backgroundScope {
                dealerLiquidationUseCase.setUpdateLiquidation(data, it).collect{response->
                    uiScope(response, _updateLiquidationItems)
                }
            }
        }
    }
}