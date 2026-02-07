package com.juagri.shared.domain.model.ledger

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Raw Firestore entry for CustomerLedger document (nested under keys "0", "1", "2", ...).
 * Used to deserialize without Map<String, Any> (which requires unavailable Any serializer).
 */
@Serializable
data class CustomerLedgerEntryRaw(
    @SerialName("chqno") val chqno: String? = null,
    @SerialName("docno") val docno: String? = null,
    @SerialName("invdate") val invdate: String? = null,
    @SerialName("cramt") val cramt: Double = 0.0,
    @SerialName("dbamt") val dbamt: Double = 0.0,
    @SerialName("monopenbal") val monopenbal: Double? = null,
    @SerialName("monclosebal") val monclosebal: Double? = null
)
