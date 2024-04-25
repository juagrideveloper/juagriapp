package com.juagri.shared.domain.repo.liquidation

import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.liquidation.DealerLiquidationData
import com.juagri.shared.domain.model.liquidation.DealerLiquidationItem
import com.juagri.shared.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface DealerLiquidationRepository {
    suspend fun getDealerLiquidationItems(tCode: String): Flow<ResponseState<DealerLiquidationData>>

    suspend fun setUpdateLiquidation(data: DealerLiquidationData,employee: JUEmployee): Flow<ResponseState<Boolean>>
}