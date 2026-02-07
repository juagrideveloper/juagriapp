package com.juagri.shared.domain.model.ledger

import kotlinx.serialization.Serializable

/**
 * Old Firestore ledger row model (chqno, docno, invdate, cramt, dbamt, balamt).
 * Used by DealerLedgerOld (old collection structure).
 */
@Serializable
data class LedgerItemOld(
    var chqno: String? = null,
    var docno: String? = null,
    var invdate: String? = null,
    var cramt: Double = 0.0,
    var dbamt: Double = 0.0,
    var balamt: Double = 0.0
)
