package com.juagri.shared.domain.repo.dashboard

import com.juagri.shared.domain.model.dashboard.DealerDashboard
import com.juagri.shared.domain.model.dashboard.DealerSales
import com.juagri.shared.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface DealerDashboardRepository {
    suspend fun getDashboard(cCode: String): Flow<ResponseState<DealerDashboard>>

    suspend fun getProductSalesReport(cCode: String): Flow<ResponseState<List<DealerSales>>>
}