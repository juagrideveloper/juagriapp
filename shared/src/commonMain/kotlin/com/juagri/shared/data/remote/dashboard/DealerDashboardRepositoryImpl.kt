package com.juagri.shared.data.remote.dashboard

import com.juagri.shared.data.local.dao.dealer.DealerDashboardDao
import com.juagri.shared.domain.model.dashboard.DealerDashboard
import com.juagri.shared.domain.model.dashboard.DealerSales
import com.juagri.shared.domain.repo.dashboard.DealerDashboardRepository
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.filterCCodeUpdatedTime
import dev.gitlive.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DealerDashboardRepositoryImpl(
    private val dashboard: CollectionReference,
    private val salesReport: CollectionReference,
    private val dealerDashboardDao: DealerDashboardDao
) : DealerDashboardRepository {
    override suspend fun getDashboard(cCode: String): Flow<ResponseState<DealerDashboard>> =
        callbackFlow {
            try {
                trySend(ResponseState.Loading(true))
                val result = dashboard.filterCCodeUpdatedTime(
                    cCode,
                    dealerDashboardDao.getDashboardLastUpdatedTime(cCode)
                )
                if (result.isNotEmpty()) {
                    dealerDashboardDao.setDealerDashboard(result.first().data())
                }
                trySend(ResponseState.Loading())
                trySend(ResponseState.Success(dealerDashboardDao.getDealerDashboard(cCode)))
            } catch (e: Exception) {
                e.printStackTrace()
                trySend(ResponseState.Error())
            }
            awaitClose {
                channel.close()
            }
        }

    override suspend fun getProductSalesReport(cCode: String): Flow<ResponseState<List<DealerSales>>> =
        callbackFlow {
            trySend(ResponseState.Loading(true))
            val result = salesReport.filterCCodeUpdatedTime(
                cCode,
                dealerDashboardDao.getProductSalesLastUpdateTime(cCode)
            )
            try {
                val salesReport: List<DealerSales> = result.map { it.data() }
                if (salesReport.isNotEmpty()) {
                    dealerDashboardDao.setProductSalesData(salesReport)
                }
                trySend(ResponseState.Loading())
                trySend(ResponseState.Success(dealerDashboardDao.getProductSalesData(cCode)))
            } catch (e: Exception) {
                e.printStackTrace()
                trySend(ResponseState.Error())
            }
            awaitClose {
                channel.close()
            }
        }
}