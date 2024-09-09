package com.juagri.shared.ui.focusProduct

import AppTypography
import Constants
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juagri.shared.domain.model.dashboard.BarType
import com.juagri.shared.domain.model.dashboard.FocusProductChartLayout
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.RowSpaceSmall
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.theme.getBarColor
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun CDOFocusProductSummary() {
    val viewModel = koinViewModel(CDOFocusProductSummaryViewModel::class)
    viewModel.setScreenId(Constants.SCREEN_CDO_FOCUS_PRODUCT)
    var isBaseApiNotCalled = true
    ScreenLayout(viewModel) {
        viewModel.apply {
            CardLayout(true, isScrollable = true) {
                CardLayout {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row {
                                Spacer(
                                    modifier = Modifier.width(16.dp).height(16.dp)
                                        .background(getColors().getBarColor(BarType.MPlan))
                                )
                                RowSpaceSmall()
                                Text(
                                    names().monthPlan +" "+names().qty,
                                    style = AppTypography.titleSmall,
                                    color = getColors().onBackground
                                )
                            }
                            ColumnSpaceSmall()
                            Row {
                                Spacer(
                                    modifier = Modifier.width(16.dp).height(16.dp)
                                        .background(getColors().getBarColor(BarType.MActual))
                                )
                                RowSpaceSmall()
                                Text(
                                    viewModel.names().monthActual +" "+names().qty,
                                    style = AppTypography.titleSmall,
                                    color = getColors().onBackground
                                )
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row {
                                Spacer(
                                    modifier = Modifier.width(16.dp).height(16.dp)
                                        .background(getColors().getBarColor(BarType.YPlan))
                                )
                                RowSpaceSmall()
                                Text(
                                    viewModel.names().yearPlan +" "+names().qty,
                                    style = AppTypography.titleSmall,
                                    color = getColors().onBackground
                                )
                            }
                            ColumnSpaceSmall()
                            Row {
                                Spacer(
                                    modifier = Modifier.width(16.dp).height(16.dp)
                                        .background(getColors().getBarColor(BarType.YActual))
                                )
                                RowSpaceSmall()
                                Text(
                                    viewModel.names().yearActual +" "+names().qty,
                                    style = AppTypography.titleSmall,
                                    color = getColors().onBackground
                                )
                            }
                        }
                    }
                }
                ColumnSpaceSmall()
                when (val result = viewModel.cdoFocusProduct.collectAsState().value) {
                    is UIState.Success -> {
                        FocusProductChartLayout(result.data.focusProducts)
                    }
                    else -> {}
                }
            }
        }
    }
    if(isBaseApiNotCalled){
        viewModel.getCDOFocusProduct()
        isBaseApiNotCalled = false
    }
}