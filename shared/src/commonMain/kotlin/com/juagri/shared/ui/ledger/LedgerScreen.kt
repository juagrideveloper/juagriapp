package com.juagri.shared.ui.ledger

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.juagri.shared.domain.model.filter.FilterType
import com.juagri.shared.ui.components.dialogs.FilterDialog
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.RowSpaceSmall
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.DropDownLayout
import com.juagri.shared.ui.components.layouts.LedgerLayout
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import com.juagri.shared.ui.components.layouts.getModifier
import com.juagri.shared.utils.UIState
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun LedgerScreen() {
    val viewModel = koinViewModel(LedgerViewModel::class)
    viewModel.setScreenId(Constants.SCREEN_LEDGER)
    ScreenLayoutWithoutActionBar {
        ScreenLayout(viewModel,true) {
            viewModel.apply {
                CardLayout {

                    Row {
                        DropDownLayout(
                            getRegionLabel(),
                            modifier = Modifier.getModifier().weight(1f)
                        ) {
                            getRegionList()
                        }
                        RowSpaceSmall()
                        DropDownLayout(
                            getTerritoryLabel(),
                            modifier = Modifier.getModifier().weight(1f)
                        ) {
                            getTerritoryList()
                        }
                    }
                    ColumnSpaceSmall()
                    Row {
                        DropDownLayout(
                            getDealerLabel(),
                            modifier = Modifier.getModifier()
                        ) {
                            getDealerList()
                        }
                    }
                    ColumnSpaceSmall()
                    Row {
                        DropDownLayout(
                            getFinYearLabel(),
                            modifier = Modifier.getModifier().weight(1f)
                        ) {
                            getFinYearList()
                        }
                        RowSpaceSmall()
                        DropDownLayout(
                            getFinMonthLabel(),
                            modifier = Modifier.getModifier().weight(1f)
                        ) {
                            getFinMonthList()
                        }
                    }
                }
                ColumnSpaceSmall()
                when (val result = viewModel.dealerLedgerItem.collectAsState().value) {
                    is UIState.Success -> {
                        result.data?.let {
                            LedgerLayout(viewModel.names(), it)
                        }
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
                    }
                }
            }
        }
    }
}