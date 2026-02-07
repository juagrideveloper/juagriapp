package com.juagri.shared.domain.model.ledger

import com.juagri.shared.domain.model.user.JUDealer
import kotlinx.serialization.Serializable

/**
 * Payload for Dealer Ledger Old PDF export (fin year, month, dealer, ledger data).
 */
@Serializable
data class ExportPDFOld(
    val finYear: String,
    val finMonth: String?,
    val dealerItem: JUDealer,
    val dealerLedgerItem: DealerLedgerItemOld
)
