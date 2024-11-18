package com.juagri.shared.ui.participation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juagri.shared.domain.model.filter.FilterType
import com.juagri.shared.ui.components.dialogs.FilterDialog
import com.juagri.shared.ui.components.fields.ColumnSpaceMedium
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.DashboardCountHeading
import com.juagri.shared.ui.components.fields.DashboardLabelHeading
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.DropDownLayout
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import com.juagri.shared.utils.Constants
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.theme.chart_m_actual_end
import com.juagri.shared.utils.theme.chart_m_plan_start
import com.juagri.shared.utils.theme.chart_y_actual_end
import com.juagri.shared.utils.theme.chart_y_plan_start
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun ParticipationScreen() {
    var isFirstTime = true
    val viewModel = koinViewModel(ParticipationViewModel::class)
    viewModel.setScreenId(Constants.SCREEN_PARTICIPATION)
    ScreenLayoutWithoutActionBar {
        ScreenLayout(viewModel, false) {
            viewModel.apply {
                CardLayout(fullHeight = true, isScrollable = true) {
                    DropDownLayout(
                        names().select,
                        mutableStateOf(
                            selectedUser.value?.name ?: names().all
                        )
                    ) {
                        getUserList()
                    }
                    /*Row {
                            Column(modifier = Modifier.weight(1f)) {
                                LabelHeading(
                                    names().event,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxHeight()
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row {
                                    LabelHeading(
                                        names().monthActual,
                                        modifier = Modifier.weight(1f)
                                    )
                                    LabelHeading(
                                        names().yearActual,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }*/
                    FilterDialog(showDialog) {
                        when (val item = it.data) {
                            is FilterType.USER -> {
                                selectedUser.value = item.data
                                if (item.data.code == "All") {
                                    getParticipationDetails(getJUEmployee()!!)
                                } else {
                                    getParticipationDetails(item.data)
                                }
                            }

                            else -> {}
                        }
                    }
                    when (val result = participationItems.collectAsState().value) {
                        is UIState.Success -> {
                            result.data.forEach { item ->
                                CardLayout {
                                    DashboardLabelHeading(item.actName)
                                    ColumnSpaceSmall()
                                    Row {
                                        DashboardCountHeading(
                                            "Y Plan",
                                            modifier = Modifier.background(getColors().chart_m_plan_start)
                                                .padding(top = 4.dp, bottom = 4.dp).fillMaxWidth()
                                                .weight(1f),
                                            textAlign = TextAlign.Center
                                        )
                                        DashboardCountHeading(
                                            "Participated",
                                            modifier = Modifier.background(getColors().chart_m_actual_end)
                                                .padding(top = 4.dp, bottom = 4.dp).fillMaxWidth()
                                                .weight(1f),
                                            textAlign = TextAlign.Center
                                        )
                                        DashboardCountHeading(
                                            "Self",
                                            modifier = Modifier.background(getColors().chart_y_plan_start)
                                                .padding(top = 4.dp, bottom = 4.dp).fillMaxWidth()
                                                .weight(1f),
                                            textAlign = TextAlign.Center
                                        )
                                        DashboardCountHeading(
                                            "Total",
                                            modifier = Modifier.background(getColors().chart_y_actual_end)
                                                .padding(top = 4.dp, bottom = 4.dp).fillMaxWidth()
                                                .weight(1f),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    println(item)
                                    Row {
                                        DashboardCountHeading(
                                            item.yPlan.toString(),
                                            modifier = Modifier.fillMaxWidth().weight(1f),
                                            textAlign = TextAlign.Center
                                        )
                                        DashboardCountHeading(
                                            item.yParticipated.toString(),
                                            modifier = Modifier.fillMaxWidth().weight(1f),
                                            textAlign = TextAlign.Center
                                        )
                                        DashboardCountHeading(
                                            item.yActual.toString(),
                                            modifier = Modifier.fillMaxWidth().weight(1f),
                                            textAlign = TextAlign.Center
                                        )
                                        DashboardCountHeading(
                                            item.yTotal.toString(),
                                            modifier = Modifier.fillMaxWidth().weight(1f),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                                ColumnSpaceMedium()
                            }
                        }

                        else -> {}
                    }
                    if (isFirstTime) {
                        isFirstTime = false
                        getParticipationDetails(getJUEmployee()!!)
                    }
                }
            }
        }
    }
}