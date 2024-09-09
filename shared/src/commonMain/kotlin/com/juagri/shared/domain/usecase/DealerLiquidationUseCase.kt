package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.liquidation.DealerLiquidationData
import com.juagri.shared.domain.repo.liquidation.DealerLiquidationRepository

class DealerLiquidationUseCase(private val repository: DealerLiquidationRepository) {
    suspend fun getDealerLiquidationItems(cdoCode: String) = repository.getDealerLiquidationItems(cdoCode)

    suspend fun setUpdateLiquidation(data: DealerLiquidationData, employee: JUEmployee) = repository.setUpdateLiquidation(data, employee)
}