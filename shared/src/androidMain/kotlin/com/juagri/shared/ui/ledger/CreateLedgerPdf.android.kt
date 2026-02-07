package com.juagri.shared.ui.ledger

import com.juagri.shared.domain.model.ledger.ExportPDFOld

/**
 * Android actual: PDF generation with iText can be added here.
 * For now returns NotAvailable; replace with real implementation using Context + iText.
 */
actual fun createLedgerPdf(exportPDF: ExportPDFOld): DealerLedgerOldPdfResult {
    return DealerLedgerOldPdfResult.NotAvailable
}
