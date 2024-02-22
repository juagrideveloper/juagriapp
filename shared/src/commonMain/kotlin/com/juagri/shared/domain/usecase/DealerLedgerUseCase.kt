package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.repo.ledger.DealerLedgerRepository
import dev.gitlive.firebase.firestore.Timestamp

class DealerLedgerUseCase(private val repository: DealerLedgerRepository) {
    suspend fun getDealerLedgerItem(cCode: String, startDate: Timestamp, endDate: Timestamp) =
        repository.getLedgerItems(cCode, startDate, endDate)
}