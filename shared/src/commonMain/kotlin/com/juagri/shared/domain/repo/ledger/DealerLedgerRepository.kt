package com.juagri.shared.domain.repo.ledger

import com.juagri.shared.domain.model.ledger.DealerLedgerItem
import com.juagri.shared.utils.ResponseState
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.coroutines.flow.Flow

interface DealerLedgerRepository {
    suspend fun getLedgerItems(cCode: String,startDate: Timestamp,endDate: Timestamp): Flow<ResponseState<DealerLedgerItem?>>
}