package com.juagri.shared.ui.ledger

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juagri.shared.ui.components.dialogs.ProgressDialog
import com.juagri.shared.ui.components.fields.ButtonFullWidth
import com.juagri.shared.ui.components.fields.ColumnSpaceMedium
import com.juagri.shared.ui.components.fields.HeadingText
import com.juagri.shared.ui.components.fields.RowSpaceMedium
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
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (result) {
                    is DealerLedgerOldPdfResult.FilePath -> {
                        HeadingText("Ledger PDF file has been created successfully!\nPlease find the file in below path:\n\nDocuments/JU_Agri_Files/Ledger/${result.fileName}", textAlign = TextAlign.Center)
                        ColumnSpaceMedium()
                        Row {
                            ButtonFullWidth(
                                text = "Share",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    viewModel.shareLedgerPDF(result.path)
                                    onClose()
                                }
                            )
                            RowSpaceMedium()
                            ButtonFullWidth(
                                text = "Back",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    onClose()
                                }
                            )
                        }

                    }
                    is DealerLedgerOldPdfResult.NotAvailable -> {
                        HeadingText("PDF export is not available on this device.", textAlign = TextAlign.Center)
                        ButtonFullWidth(
                            text = "Back",
                            onClick = {
                                onClose()
                            }
                        )
                    }
                }
            }
        }
        else -> {}
    }
}
