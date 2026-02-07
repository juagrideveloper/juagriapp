package com.juagri.shared.ui.ledger

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.ledger.ExportPDFOld
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Result of PDF creation: file path (Android) or not-available (iOS/stub).
 */
sealed class DealerLedgerOldPdfResult {
    data class FilePath(val path: String, val fileName: String) : DealerLedgerOldPdfResult()
    data object NotAvailable : DealerLedgerOldPdfResult()
}

class DealerLedgerOldExportToPDFViewModel(
    session: SessionPreference,
    dataManager: DataManager,
) : BaseViewModel(session, dataManager) {
    private val _pdfFile = MutableStateFlow<UIState<DealerLedgerOldPdfResult>>(UIState.Init)
    val pdfFile = _pdfFile.asStateFlow()

    fun createPdf() {
        val data = DealerLedgerOldExportHolder.exportData
        if (data == null) {
            _pdfFile.value = UIState.Success(DealerLedgerOldPdfResult.NotAvailable)
            return
        }
        //_pdfFile.value = UIState.Loading()
        val result = createLedgerPdf(data)
        _pdfFile.value = UIState.Success(result)
    }

    fun clearExportData() {
        DealerLedgerOldExportHolder.exportData = null
    }
}

/**
 * Platform-specific PDF creation. Android: iText; iOS: NotAvailable.
 */
expect fun createLedgerPdf(exportPDF: ExportPDFOld): DealerLedgerOldPdfResult
