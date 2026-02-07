package com.juagri.shared.domain.repo.ledger

import com.juagri.shared.domain.model.ledger.DealerLedgerItemOld
import com.juagri.shared.domain.model.ledger.LedgerInvoiceItemOld
import kotlinx.coroutines.flow.Flow

interface DealerLedgerOldRepository {
    fun getDealerLedger(dealerCode: String, finYear: String, month: String = ""): Flow<DealerLedgerItemOld>
    fun getInvoiceDetails(invoiceNo: String): Flow<LedgerInvoiceItemOld?>
}
