package com.juagri.shared.ui.dashboard.cdo

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.promotion.PromotionDashboard
import com.juagri.shared.domain.usecase.PromotionUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CDODashboardViewModel(
    dataManager: DataManager,
    session: SessionPreference,
    private val promotionUseCase: PromotionUseCase
) : BaseViewModel(session, dataManager) {

    private var _promotionDashboardItems: MutableStateFlow<UIState<List<PromotionDashboard>>> =
        MutableStateFlow(UIState.Init)
    val promotionDashboardItems = _promotionDashboardItems.asStateFlow()

    fun resetScreen(){
        _promotionDashboardItems.value = UIState.Init
    }
    fun getDashboard(){
        getJUEmployee()?.let {employee->
            backgroundScope {
                promotionUseCase.getDashboard(employee).collect { response ->
                    uiScope(response, _promotionDashboardItems)
                }
            }
        }
    }

}