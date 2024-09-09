package com.juagri.shared.ui.participation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.juagri.shared.ui.components.fields.ColumnSpaceMedium
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.LabelContent
import com.juagri.shared.ui.components.fields.LabelHeading
import com.juagri.shared.ui.components.fields.TextSmall
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.LedgerLayout
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import com.juagri.shared.ui.ledger.LedgerViewModel
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.toDDMMYYYY
import com.juagri.shared.utils.toMoneyFormat
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun ParticipationScreen() {
    val viewModel = koinViewModel(ParticipationViewModel::class)
    viewModel.setScreenId(Constants.SCREEN_PARTICIPATION)
    ScreenLayoutWithoutActionBar {
        ScreenLayout(viewModel, false) {
            CardLayout(fullHeight = true, isScrollable = true) {
                CardLayout {
                    Row {
                        Column(modifier = Modifier.weight(1f)) {
                            LabelHeading(viewModel.names().event, textAlign = TextAlign.Center, modifier = Modifier.fillMaxHeight())
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row {
                                LabelHeading(viewModel.names().monthActual, modifier = Modifier.weight(1f))
                                LabelHeading(viewModel.names().yearActual, modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
                when (val result = viewModel.participationItems.collectAsState().value) {
                    is UIState.Success -> {
                        result.data.forEach { item ->
                            ColumnSpaceMedium()
                            CardLayout {
                                Row {
                                    Column(modifier = Modifier.weight(1f)) {
                                        LabelContent(item.actName, textAlign = TextAlign.Center)
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row {
                                            LabelContent(item.mCount.toMoneyFormat(), modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                            LabelContent(item.yCount.toMoneyFormat(), modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    else -> {}
                }
                var isFirstTime = true
                if (isFirstTime) {
                    isFirstTime = false
                    viewModel.getParticipationDetails()
                }
            }
        }
    }
}