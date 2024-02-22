package com.juagri.shared.ui.dashboard.dealer

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.juagri.shared.domain.model.dashboard.DealerDashboard
import com.juagri.shared.domain.model.dashboard.OSChartItem
import com.juagri.shared.domain.model.dashboard.OSType
import com.juagri.shared.ui.components.fields.RowSpaceSmall
import com.juagri.shared.ui.components.fields.TextTitle
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.OSChartLayout
import com.juagri.shared.ui.components.layouts.ProductSalesReport
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.theme.chart_os_g180
import com.juagri.shared.utils.theme.chart_os_l120
import com.juagri.shared.utils.theme.chart_os_l180
import com.juagri.shared.utils.theme.chart_os_l90
import com.juagri.shared.utils.value
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun DealerDashboardScreen() {
    val viewModel = koinViewModel(DealerDashboardViewModel::class)
    ScreenLayoutWithoutActionBar(modifier = Modifier.background(Color.LightGray)) {
        ScreenLayout(viewModel,true) {
            var isDashboardNotCreated = true
            when (val result = viewModel.dealerDashboard.collectAsState().value) {
                is UIState.Success -> {
                    result.data.let {
                        OSChart(it)
                    }
                }

                else -> {}
            }
            when (val result = viewModel.productSalesReport.collectAsState().value) {
                is UIState.Success -> {
                    result.data.let {
                        ProductSalesReport(it)
                    }
                }

                else -> {}
            }
            if(isDashboardNotCreated){
                isDashboardNotCreated = false
                viewModel.getDashboard()
            }
        }
    }
}

@Composable
private fun OSChart(dealerDashboard: DealerDashboard){
    val osItems = listOf<OSChartItem>(
        OSChartItem(
            OSType.L90,
            "L90",
            dealerDashboard.l90.value().toFloat(),
            getColors().chart_os_l90
        ),
        OSChartItem(
            OSType.L120,
            "L120",
            dealerDashboard.l120.value().toFloat(),
            getColors().chart_os_l120
        ),
        OSChartItem(
            OSType.L180,
            "L180",
            dealerDashboard.l180.value().toFloat(),
            getColors().chart_os_l180
        ),
        OSChartItem(
            OSType.G180,
            "G180",
            dealerDashboard.g180.value().toFloat(),
            getColors().chart_os_g180
        )
    )
    CardLayout {
        TextTitle("OutStanding")
        RowSpaceSmall()
        OSChartLayout(osItems)
    }
    RowSpaceSmall()
}