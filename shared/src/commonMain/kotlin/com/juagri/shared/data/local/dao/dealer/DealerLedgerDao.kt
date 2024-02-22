package com.juagri.shared.data.local.dao.dealer

import com.juagri.shared.DealerDetailsQueries
import com.juagri.shared.domain.model.ledger.DealerLedgerItem
import com.juagri.shared.domain.model.ledger.LedgerItem
import com.juagri.shared.domain.model.ledger.LedgerOpeningBalance
import com.juagri.shared.utils.isDeleted
import com.juagri.shared.utils.toTimeStamp
import com.juagri.shared.utils.toYYYYMMDD
import com.juagri.shared.utils.value

class DealerLedgerDao(private val queries: DealerDetailsQueries) {

    fun insertLedger(ledgerItems:List<LedgerItem>){
        ledgerItems.forEach {
            if(it.status.isDeleted()){
                queries.deleteLedger(it.id.value())
            }else {
                queries.insertLedger(
                    id = it.id.value(),
                    post_date = it.postDate.value(),
                    ccode = it.custCode,
                    doc_no = it.docNo,
                    cheque_no = it.chequeNo,
                    debit_amt = it.debitAmt,
                    credit_amt = it.creditAmt,
                    updatedTime = it.updatedTime.value()
                )
            }
        }
    }

    fun insertOpeningBalance(ledgerItems:List<LedgerOpeningBalance>){
        ledgerItems.forEach {
            if(it.status.isDeleted()){
                queries.deleteOpeningBalance(it.id.value())
            }else {
                queries.insertOpeningBalance(
                    id = it.id.value(),
                    month = it.month.value(),
                    ccode = it.cCode.value(),
                    open_balance = it.openingBalance.value(),
                    updatedTime = it.updatedTime.value()
                )
            }
        }
    }

    fun getLedgerItems(cCode: String, startDate: Double, endDate: Double): DealerLedgerItem? =
        try {
            val openingBalance =
                queries.getOpeningBalance(cCode, startDate.toTimeStamp().toYYYYMMDD())
                    .executeAsOne().open_balance.value()
            var closingBalance = openingBalance
            var debitAmount = 0.0
            var creditAmount = 0.0
            val ledgerItems =
                queries.getLedgerItems(cCode, startDate, endDate).executeAsList().map {
                    closingBalance = (closingBalance + it.credit_amt.value()) - it.debit_amt.value()
                    debitAmount = it.debit_amt.value()
                    creditAmount = it.credit_amt.value()
                    LedgerItem(
                        id = it.id.value(),
                        postDate = it.post_date.toTimeStamp(),
                        custCode = it.ccode,
                        docNo = it.doc_no,
                        chequeNo = it.cheque_no,
                        debitAmt = it.debit_amt,
                        creditAmt = it.credit_amt,
                        balanceAmt = closingBalance,
                        updatedTime = it.updatedTime.toTimeStamp()
                    )
                }
            DealerLedgerItem(
                ledgerItems = ledgerItems,
                openingBalance = openingBalance,
                closingBalance = closingBalance,
                totalDebit = debitAmount,
                totalCredit = creditAmount
            )
        }catch (e: Exception) {
            null
        }

    fun getLedgerLastUpdatedTime(cCode: String): Double= queries.getLedgerLastUpdatedTime(cCode).executeAsOne().max.value()

    fun getOpeningBalanceLastUpdatedTime(cCode: String): Double= queries.getOpeningBalanceLastUpdatedTime(cCode).executeAsOne().max.value()
}