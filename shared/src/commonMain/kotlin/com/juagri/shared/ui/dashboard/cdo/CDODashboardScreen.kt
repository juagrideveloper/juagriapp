package com.juagri.shared.ui.dashboard.cdo

import Constants
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juagri.shared.domain.model.promotion.PromotionDashboard
import com.juagri.shared.ui.components.fields.ColumnSpaceMedium
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.DashboardCountHeading
import com.juagri.shared.ui.components.fields.DashboardLabelHeading
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.theme.chart_m_actual_end
import com.juagri.shared.utils.theme.chart_m_plan_start
import com.juagri.shared.utils.theme.chart_y_actual_end
import com.juagri.shared.utils.theme.chart_y_plan_start
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun CDODashboardScreen() {
    val viewModel = koinViewModel(CDODashboardViewModel::class)
    viewModel.setScreenId(Constants.SCREEN_DASHBOARD)
    ScreenLayout(viewModel) {
        viewModel.apply {
            var isDashboardNotCreated = true
            CardLayout(true, isScrollable = true) {
                when (val result = viewModel.promotionDashboardItems.collectAsState().value) {
                    is UIState.Success -> {
                        updatePromotionEntryCount(result.data)
                    }
                    else -> {}
                }
            }
            if(isDashboardNotCreated){
                isDashboardNotCreated = false
                resetScreen()
                getDashboard()
            }
        }
    }
}

@Composable
private fun updatePromotionEntryCount(list: List<PromotionDashboard>){
    list.forEach {
        CardLayout {
            DashboardLabelHeading(it.actName)
            ColumnSpaceSmall()
            Row {
                DashboardCountHeading(
                    "M Plan",
                    modifier = Modifier.background(getColors().chart_m_plan_start).padding(top = 4.dp, bottom = 4.dp).fillMaxWidth().weight(1f),
                    textAlign = TextAlign.Center
                )
                DashboardCountHeading(
                    "M Actual",
                    modifier = Modifier.background(getColors().chart_m_actual_end).padding(top = 4.dp, bottom = 4.dp).fillMaxWidth().weight(1f),
                    textAlign = TextAlign.Center
                )
                DashboardCountHeading(
                    "Y Plan",
                    modifier = Modifier.background(getColors().chart_y_plan_start).padding(top = 4.dp, bottom = 4.dp).fillMaxWidth().weight(1f),
                    textAlign = TextAlign.Center
                )
                DashboardCountHeading(
                    "Y Actual",
                    modifier = Modifier.background(getColors().chart_y_actual_end).padding(top = 4.dp, bottom = 4.dp).fillMaxWidth().weight(1f),
                    textAlign = TextAlign.Center
                )
            }
            Row {
                DashboardCountHeading(
                    it.mPlan.toString(),
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    textAlign = TextAlign.Center
                )
                DashboardCountHeading(
                    it.mActual.toString(),
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    textAlign = TextAlign.Center
                )
                DashboardCountHeading(
                    it.yPlan.toString(),
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    textAlign = TextAlign.Center
                )
                DashboardCountHeading(
                    it.yActual.toString(),
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
        ColumnSpaceMedium()
    }
}