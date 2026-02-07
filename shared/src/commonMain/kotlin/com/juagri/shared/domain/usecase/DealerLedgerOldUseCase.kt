package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.model.ledger.LedgerInvoiceItemOld
import com.juagri.shared.domain.repo.ledger.DealerLedgerOldRepository
import kotlinx.coroutines.flow.Flow

class DealerLedgerOldUseCase(private val repository: DealerLedgerOldRepository) {
    fun getDealerLedger(dealerCode: String, finYear: String, month: String = "") =
        repository.getDealerLedger(dealerCode, finYear, month)
    fun getInvoiceDetails(invoiceNo: String): Flow<LedgerInvoiceItemOld?> =
        repository.getInvoiceDetails(invoiceNo)
}
