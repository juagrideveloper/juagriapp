package com.juagri.shared.data.remote.ledger

import com.juagri.shared.data.local.dao.dealer.DealerLedgerDao
import com.juagri.shared.domain.model.ledger.DealerLedgerItem
import com.juagri.shared.domain.model.ledger.LedgerItem
import com.juagri.shared.domain.model.ledger.LedgerOpeningBalance
import com.juagri.shared.domain.repo.ledger.DealerLedgerRepository
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.endTime
import com.juagri.shared.utils.filterCCodeUpdatedTime
import com.juagri.shared.utils.startTime
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DealerLedgerRepositoryImpl(
    private val openingBalanceDB: CollectionReference,
    private val ledgerDB: CollectionReference,
    private val dealerLedgerDao: DealerLedgerDao
) : DealerLedgerRepository {

    override suspend fun getLedgerItems(
        cCode: String,
        startDate: Timestamp,
        endDate: Timestamp
    ): Flow<ResponseState<DealerLedgerItem?>> =
        callbackFlow {
            trySend(ResponseState.Loading(true))
            val openingBalances = openingBalanceDB.filterCCodeUpdatedTime(
                cCode,
                dealerLedgerDao.getOpeningBalanceLastUpdatedTime(cCode)
            )
            val openingBalanceList: List<LedgerOpeningBalance> = openingBalances.map { it.data() }
            if (openingBalanceList.isNotEmpty()) {
                dealerLedgerDao.insertOpeningBalance(openingBalanceList)
            }
            val ledgerItems = ledgerDB.filterCCodeUpdatedTime(
                cCode,
                dealerLedgerDao.getLedgerLastUpdatedTime(cCode)
            )
            val ledgerItemList: List<LedgerItem> = ledgerItems.map { it.data() }
            if (ledgerItemList.isNotEmpty()) {
                dealerLedgerDao.insertLedger(ledgerItemList)
            }
            trySend(ResponseState.Loading())
            trySend(
                ResponseState.Success(
                    dealerLedgerDao.getLedgerItems(
                        cCode,
                        startDate.startTime(),
                        endDate.endTime()
                    )
                )
            )
            awaitClose {
                channel.close()
            }
        }
}