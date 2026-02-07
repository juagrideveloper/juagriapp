package com.juagri.shared.domain.model.ledger

import kotlinx.serialization.Serializable

/**
 * Old Firestore dealer ledger aggregate (opening/closing balance, items, debit/credit totals).
 * Used by DealerLedgerOld (old collection structure).
 */
@Serializable
data class DealerLedgerItemOld(
    val openingBalanceAmount: Double,
    var closingBalanceAmount: Double,
    val ledgerItems: List<LedgerItemOld>,
    var debit: Double = 0.0,
    var credit: Double = 0.0
)
