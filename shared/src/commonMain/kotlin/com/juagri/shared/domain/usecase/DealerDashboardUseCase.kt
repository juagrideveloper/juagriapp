package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.repo.dashboard.DealerDashboardRepository

class DealerDashboardUseCase(private val repository: DealerDashboardRepository) {
    suspend fun getDashboard(cCode: String) = repository.getDashboard(cCode)

    suspend fun getProductSalesReport(cCode: String) = repository.getProductSalesReport(cCode)
}