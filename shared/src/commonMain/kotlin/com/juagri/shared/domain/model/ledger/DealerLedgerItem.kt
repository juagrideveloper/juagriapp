package com.juagri.shared.domain.model.ledger

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LedgerItem(
    @SerialName("id") val id:String? =null,
    @SerialName("post_date") val postDate:Timestamp? = null,
    @SerialName("ccode") val custCode:String? = null,
    @SerialName("doc_no") val docNo: String? = null,
    @SerialName("cheque_no") val chequeNo: String? = null,
    @SerialName("debit_amt") val debitAmt: Double? = null,
    @SerialName("credit_amt") val creditAmt:Double? = null,
    @SerialName("balance_amt") val balanceAmt:Double? = null,
    @SerialName("status") val status:Double? = null,
    @SerialName("updatedTime") val updatedTime:Timestamp? = null
)

@Serializable
data class LedgerOpeningBalance(
    @SerialName("id") val id:String? =null,
    @SerialName("ccode") val cCode:String? = null,
    @SerialName("month") val month:String? = null,
    @SerialName("open_balance") val openingBalance: Double? = null,
    @SerialName("status") val status:Double? = null,
    @SerialName("updatedTime") val updatedTime:Timestamp? = null
)

@Serializable
data class DealerLedgerItem(
    val ledgerItems: List<LedgerItem>? = null,
    val openingBalance: Double?= null,
    val closingBalance: Double?= null,
    val totalDebit: Double?= null,
    val totalCredit: Double?= null
)