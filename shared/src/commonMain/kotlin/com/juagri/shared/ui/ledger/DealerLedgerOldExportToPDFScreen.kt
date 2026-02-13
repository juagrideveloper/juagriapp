package com.juagri.shared.ui.ledger

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juagri.shared.ui.components.dialogs.ProgressDialog
import com.juagri.shared.utils.Constants
import com.juagri.shared.utils.UIState
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun DealerLedgerOldExportToPDFScreen(onClose: () -> Unit) {
    val viewModel = koinViewModel(DealerLedgerOldExportToPDFViewModel::class)
    viewModel.setScreenId(Constants.SCREEN_DEALER_LEDGER_EXPORT)
    val pdfState by viewModel.pdfFile.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.createPdf()
    }
    val showProgress = remember { mutableStateOf(false) }

    ProgressDialog(showProgress)
    when (val state = pdfState) {
        is UIState.Success -> {
            val result = state.data
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    when (result) {
                        is DealerLedgerOldPdfResult.FilePath ->
                            "PDF saved: ${result.fileName}\n${result.path}"
                        is DealerLedgerOldPdfResult.NotAvailable ->
                            "PDF export is not available on this device."
                    }
                )
                when (result) {
                    is DealerLedgerOldPdfResult.FilePath ->
                        Button(
                            onClick = {
                                //viewModel.clearExportData()
                                viewModel.shareLedgerPDF(result.path)
                                onClose()
                            },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Share")
                        }
                    else -> {}
                }
            }
        }
        else -> {}
    }
}
