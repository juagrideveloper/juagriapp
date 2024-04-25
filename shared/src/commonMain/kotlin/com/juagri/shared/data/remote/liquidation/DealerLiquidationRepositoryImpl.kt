package com.juagri.shared.data.remote.liquidation

import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.liquidation.DealerLiquidationConfig
import com.juagri.shared.domain.model.liquidation.DealerLiquidationData
import com.juagri.shared.domain.model.liquidation.DealerLiquidationItem
import com.juagri.shared.domain.repo.liquidation.DealerLiquidationRepository
import com.juagri.shared.utils.JUError
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.uploadImages
import com.juagri.shared.utils.value
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DealerLiquidationRepositoryImpl(
    private val config: CollectionReference,
    private val liquidationDB: CollectionReference
): DealerLiquidationRepository {
    override suspend fun getDealerLiquidationItems(tCode: String): Flow<ResponseState<DealerLiquidationData>> =
        callbackFlow {
            trySend(ResponseState.Loading(true))
            try {
                val configItem = config.document(Constants.TABLE_LIQUIDATION_CONFIG).get().data<DealerLiquidationConfig>()
                val liquidationItems = liquidationDB.where { "tcode" equalTo tCode }.get().documents.map { it.data<DealerLiquidationItem>() }
                trySend(ResponseState.Loading())
                trySend(
                    ResponseState.Success(
                        DealerLiquidationData(
                            configItem,
                            liquidationItems
                        )
                    )
                )
            }catch (e: Exception){
                e.printStackTrace()
                trySend(ResponseState.Error())
            }
            awaitClose {
                channel.close()
            }
        }

    override suspend fun setUpdateLiquidation(
        data: DealerLiquidationData,
        employee: JUEmployee
    ): Flow<ResponseState<Boolean>> = callbackFlow {
        trySend(ResponseState.Loading(true))
        try {
            data.liquidationItems.forEach {dealerItem ->
                val brandItems = dealerItem.brandItems.map {
                    mapOf(
                        "bcode" to it.bCode,
                        "bname" to it.bName,
                        "pqty" to it.pQty,
                        "stock" to it.stockItem.value
                    )
                }
                val updatedItems = mapOf(
                    "ccode" to dealerItem.cCode,
                    "cname" to dealerItem.cName,
                    "regcode" to dealerItem.regCode,
                    "tcode" to dealerItem.tCode,
                    "status" to 1.0,
                    "branditems" to brandItems,
                    "updated_empcode" to employee.code.value(),
                    "updated_emprole" to employee.roleId.value(),
                    "updated_empname" to employee.name.value(),
                    "updated_weekno" to data.config.weekNo,
                    "stock_updated_time" to FieldValue.serverTimestamp,
                )
                println(updatedItems)
                liquidationDB.document(dealerItem.cCode).update(updatedItems)
            }
            trySend(ResponseState.Loading())
            trySend(ResponseState.Success(true))
        } catch (e: Exception) {
            e.printStackTrace()
            trySend(ResponseState.Loading())
            trySend(ResponseState.Error())
        }
        awaitClose {
            channel.close()
        }
    }
}