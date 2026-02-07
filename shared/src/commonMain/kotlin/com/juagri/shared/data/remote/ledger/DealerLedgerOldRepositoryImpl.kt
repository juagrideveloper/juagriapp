package com.juagri.shared.data.remote.ledger

import com.juagri.shared.domain.model.ledger.CustomerLedgerEntryRaw
import com.juagri.shared.domain.model.ledger.DealerLedgerItemOld
import com.juagri.shared.domain.model.ledger.LedgerInvoiceItemOld
import com.juagri.shared.domain.model.ledger.LedgerItemOld
import com.juagri.shared.domain.model.ledger.OptionalLedgerEntryRaw
import com.juagri.shared.domain.repo.ledger.DealerLedgerOldRepository
import com.juagri.shared.utils.value
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DealerLedgerOldRepositoryImpl(
    private val dealerLedgerOld: CollectionReference,
    private val ledgerInvoiceOld: CollectionReference
) : DealerLedgerOldRepository {

    override fun getDealerLedger(
        dealerCode: String,
        finYear: String,
        month: String
    ): Flow<DealerLedgerItemOld> = callbackFlow {
        var openingBalance = 0.0
        var closingBalance = 0.0
        val ledgerItems = mutableListOf<LedgerItemOld>()
        val monthValue = month.value().ifEmpty { "ALL" }
        println("DealerLedgerOldRepository: getDealerLedger dealerCode=$dealerCode finYear=$finYear monthValue=$monthValue")
        try {
            fun processOneDoc(docData: DocData?, openingTaken: BooleanArray) {
                if (docData == null) return
                val data = docData.entries
                // Use root-level or entry-"0" opening/closing first so we don't rely only on entry objects
                docData.openingFromRoot?.let {
                    if (!openingTaken[0]) { openingBalance = it; openingTaken[0] = true }
                }
                docData.closingFromRoot?.let { closingBalance = it }
                println("DealerLedgerOldRepository: processOneDoc entries=${data.size}")
                data.keys.sorted().forEach { key ->
                    val row = data[key] ?: return@forEach
                    row.monopenbal?.let {
                        if (!openingTaken[0]) { openingBalance = it; openingTaken[0] = true }
                    }
                    row.monclosebal?.let { closingBalance = it }
                    if (row.docno != null || row.invdate != null) {
                        ledgerItems.add(
                            LedgerItemOld(
                                chqno = row.chqno.takeIf { !it.isNullOrEmpty() },
                                docno = row.docno.takeIf { !it.isNullOrEmpty() },
                                invdate = row.invdate.takeIf { !it.isNullOrEmpty() },
                                cramt = row.cramt,
                                dbamt = row.dbamt,
                                balamt = 0.0
                            )
                        )
                    }
                }
            }

            fun safeDocData(doc: DocumentSnapshot): DocData? =
                try {
                    val raw = doc.data<Map<String, OptionalLedgerEntryRaw>>()
                    val entries = raw.mapNotNull { (k, v) -> v.entry?.let { k to it } }.toMap()
                    val skipped = raw.size - entries.size
                    if (skipped > 0) {
                        println("DealerLedgerOldRepository: safeDocData docId=${doc.id} totalKeys=${raw.size} parsed=${entries.size} skipped=$skipped (non-object values)")
                    }
                    // Opening/closing may be root-level (e.g. "openingBalance", "monopenbal") or entry "0" stored as a number
                    val openingFromRoot = raw["openingBalance"]?.doubleValue
                        ?: raw["monopenbal"]?.doubleValue
                        ?: raw["0"]?.doubleValue
                    val closingFromRoot = raw["closingBalance"]?.doubleValue
                        ?: raw["monclosebal"]?.doubleValue
                    if (openingFromRoot != null || closingFromRoot != null) {
                        println("DealerLedgerOldRepository: safeDocData docId=${doc.id} openingFromRoot=$openingFromRoot closingFromRoot=$closingFromRoot")
                    }
                    DocData(entries = entries, openingFromRoot = openingFromRoot, closingFromRoot = closingFromRoot)
                } catch (e: Exception) {
                    println("DealerLedgerOldRepository: safeDocData docId=${doc.id} decode failed: ${e.message}")
                    e.printStackTrace()
                    null
                }

            if (monthValue == "ALL") {
                val yearCol = dealerLedgerOld.document(dealerCode).collection(finYear)
                val documents = yearCol.get().documents
                val sortedDocs = documents.sortedBy { it.id }
                println("DealerLedgerOldRepository: month=ALL documents=${documents.size} docIds=${sortedDocs.map { it.id }}")
                val openingTaken = booleanArrayOf(false)
                for (doc in sortedDocs) {
                    val docData = safeDocData(doc)
                    if (docData == null) println("DealerLedgerOldRepository: doc ${doc.id} -> null (skipped)")
                    processOneDoc(docData, openingTaken)
                }
            } else {
                val docRef =
                    dealerLedgerOld.document(dealerCode).collection(finYear).document(monthValue)
                val snap = docRef.get()
                println("DealerLedgerOldRepository: single month docId=${snap.id} exists=${snap.exists}")
                val docData = safeDocData(snap)
                if (docData == null) println("DealerLedgerOldRepository: doc ${snap.id} -> null (skipped)")
                processOneDoc(docData, booleanArrayOf(false))
            }
            println("DealerLedgerOldRepository: result openingBalance=$openingBalance closingBalance=$closingBalance ledgerItems=${ledgerItems.size}")
            trySend(DealerLedgerItemOld(openingBalance, closingBalance, ledgerItems))
        } catch (e: Exception) {
            println("DealerLedgerOldRepository: getDealerLedger failed: ${e.message}")
            e.printStackTrace()
            trySend(DealerLedgerItemOld(0.0, 0.0, emptyList()))
        }
        awaitClose { channel.close() }
    }

    override fun getInvoiceDetails(invoiceNo: String): Flow<LedgerInvoiceItemOld?> = callbackFlow {
        println("DealerLedgerOldRepository: getInvoiceDetails invoiceNo=$invoiceNo")
        try {
            val snap = ledgerInvoiceOld.document(invoiceNo).get()
            val item = snap.data<LedgerInvoiceItemOld>()
            println("DealerLedgerOldRepository: getInvoiceDetails success=$item")
            trySend(item)
        } catch (e: Exception) {
            println("DealerLedgerOldRepository: getInvoiceDetails failed: ${e.message}")
            trySend(null)
        }
        awaitClose { channel.close() }
    }
}
data class DocData(
    val entries: Map<String, CustomerLedgerEntryRaw>,
    val openingFromRoot: Double?,
    val closingFromRoot: Double?
)