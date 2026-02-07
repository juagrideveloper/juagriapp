package com.juagri.shared.ui.ledger

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.juagri.shared.domain.model.filter.FilterType
import com.juagri.shared.domain.model.ledger.ExportPDFOld
import com.juagri.shared.ui.components.dialogs.FilterDialog
import com.juagri.shared.ui.components.dialogs.ProgressDialog
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.RowSpaceSmall
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.DropDownLayout
import com.juagri.shared.ui.components.layouts.LedgerOldLayout
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import com.juagri.shared.ui.components.layouts.getModifier
import com.juagri.shared.utils.Constants
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.value
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun DealerLedgerOldScreen(
    onExportToPdf: (ExportPDFOld) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val viewModel = koinViewModel(DealerLedgerOldViewModel::class)
    viewModel.setScreenId(Constants.SCREEN_DEALER_LEDGER_OLD)
    val ledgerState by viewModel.dealerLedgerItem.collectAsState()
    val invoiceState by viewModel.ledgerInvoice.collectAsState()
    ScreenLayoutWithoutActionBar {
        ScreenLayout(viewModel, true) {
            viewModel.apply {
                ProgressDialog(z0001)
                CardLayout {
                    if (Constants.CURRENT_APP_MODE != Constants.APP_MODE_DEALER) {
                        Row {
                            DropDownLayout(
                                getRegionLabel(),
                                modifier = Modifier.getModifier().weight(1f)
                            ) { getRegionList() }
                            RowSpaceSmall()
                            DropDownLayout(
                                getTerritoryLabel(),
                                modifier = Modifier.getModifier().weight(1f)
                            ) { getTerritoryList() }
                        }
                        ColumnSpaceSmall()
                        Row {
                            DropDownLayout(
                                getDealerLabel(),
                                modifier = Modifier.getModifier()
                            ) { getDealerList() }
                        }
                        ColumnSpaceSmall()
                    }
                    Row {
                        DropDownLayout(
                            getFinYearLabel(),
                            modifier = Modifier.getModifier().weight(1f)
                        ) { getFinYearList() }
                        DropDownLayout(
                            getFinMonthLabel(),
                            modifier = Modifier.getModifier().weight(1f)
                        ) { getFinMonthList() }
                    }
                    ColumnSpaceSmall()
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                val dealer = selectedDealer.value ?: return@Button
                                val finYear = selectedFinYear.value ?: return@Button
                                val finMonth = selectedFinMonth.value
                                when (val s = ledgerState) {
                                    is UIState.Success -> s.data?.let { ledger ->
                                        if (ledger.ledgerItems.isNotEmpty()) {
                                            onExportToPdf(
                                                ExportPDFOld(
                                                    finYear = finYear.fYear.value(),
                                                    finMonth = finMonth?.fMonth.value(),
                                                    dealerItem = dealer,
                                                    dealerLedgerItem = ledger
                                                )
                                            )
                                        } else showErrorMessage("Your ledger is empty!")
                                    } ?: showErrorMessage("Your ledger is empty!")
                                    else -> showErrorMessage("Please load ledger first.")
                                }
                            }
                        ) {
                            Text("Export to PDF")
                        }
                    }
                }
                ColumnSpaceSmall()
                when (ledgerState) {
                    is UIState.Success -> (ledgerState as UIState.Success).data?.let {
                        LedgerOldLayout(
                            names = names(),
                            dealerLedgerItem = it,
                            onInvoiceClick = { docno -> getLedgerInvoice(docno) }
                        )
                    }
                    else -> {}
                }
                FilterDialog(showDialog) {
                    resetLedger()
                    when (val item = it.data) {
                        is FilterType.REGION -> {
                            selectedRegion.value = item.data
                            selectedTerritory.value = null
                            selectedDealer.value = null
                        }
                        is FilterType.TERRITORY -> {
                            selectedTerritory.value = item.data
                            selectedDealer.value = null
                        }
                        is FilterType.DEALER -> {
                            selectedDealer.value = item.data
                            getLedgerDetails()
                        }
                        is FilterType.FIN_YEAR -> {
                            selectedFinYear.value = item.data
                            selectedFinMonth.value = null
                            getLedgerDetails()
                        }
                        is FilterType.FIN_MONTH -> {
                            selectedFinMonth.value = item.data
                            getLedgerDetails()
                        }
                        else -> {}
                    }
                }
                when (val inv = invoiceState) {
                    is UIState.Success -> inv.data?.let { invoice ->
                        LedgerInvoiceDialog(
                            invoice = invoice,
                            onDismiss = { clearInvoiceState() }
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}
