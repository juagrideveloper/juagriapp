package com.juagri.shared.ui.dashboard.dealer

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.dashboard.DealerDashboard
import com.juagri.shared.domain.model.dashboard.DealerSales
import com.juagri.shared.domain.usecase.DealerDashboardUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DealerDashboardViewModel(
    private val session: SessionPreference,
    private val dataManager: DataManager,
    private val dealerDashboardUseCase: DealerDashboardUseCase
) : BaseViewModel(session, dataManager) {

    private var _dealerDashboard: MutableStateFlow<UIState<DealerDashboard>> =
        MutableStateFlow(UIState.Init)
    val dealerDashboard = _dealerDashboard.asStateFlow()

    private var _productSalesReport: MutableStateFlow<UIState<List<DealerSales>>> =
        MutableStateFlow(UIState.Init)
    val productSalesReport = _productSalesReport.asStateFlow()

    fun getDashboard() {
        setDemoUser()
        backgroundScope{
            dealerDashboardUseCase.getDashboard(session.empCode()).collect{ response ->
                uiScope(response,_dealerDashboard)
            }
        }

        backgroundScope{
            dealerDashboardUseCase.getProductSalesReport(session.empCode()).collect{response ->
                uiScope(response,_productSalesReport)
            }
        }
    }
}