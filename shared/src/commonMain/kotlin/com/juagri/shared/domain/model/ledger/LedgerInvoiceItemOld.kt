package com.juagri.shared.domain.model.ledger

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Invoice detail from old Firestore LedgerInvoice collection (for invoice dialog).
 * Adjust fields to match actual Firestore document structure.
 */
@Serializable
data class LedgerInvoiceItemOld(
    @SerialName("docno") var docNo: String? = null,
    @SerialName("invdate") var invDate: String? = null,
    @SerialName("chqno") var chqNo: String? = null,
    @SerialName("dbamt") var debitAmt: Double? = null,
    @SerialName("cramt") var creditAmt: Double? = null,
    @SerialName("items") var items: List<LedgerInvoiceLineItemOld>? = null
)

@Serializable
data class LedgerInvoiceLineItemOld(
    @SerialName("pname") var productName: String? = null,
    @SerialName("qty") var qty: Double? = null,
    @SerialName("rate") var rate: Double? = null,
    @SerialName("amt") var amount: Double? = null
)
