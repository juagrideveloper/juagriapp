package com.juagri.shared.ui.ledger

import com.juagri.shared.domain.model.ledger.ExportPDFOld

/**
 * Holder for export data when navigating to DealerLedgerOldExportToPDFScreen
 * (avoids passing large Parcelable through navigation).
 */
object DealerLedgerOldExportHolder {
    var exportData: ExportPDFOld? = null
}
