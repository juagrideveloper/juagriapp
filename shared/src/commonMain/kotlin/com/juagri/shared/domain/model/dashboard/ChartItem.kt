package com.juagri.shared.domain.model.dashboard

import com.juagri.shared.utils.getColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.juagri.shared.utils.theme.chart_m_actual_end
import com.juagri.shared.utils.theme.chart_m_actual_start
import com.juagri.shared.utils.theme.chart_m_plan_end
import com.juagri.shared.utils.theme.chart_m_plan_start
import com.juagri.shared.utils.theme.chart_y_actual_end
import com.juagri.shared.utils.theme.chart_y_actual_start

data class OSChartItem(val type: OSType,val label: String,val value: Float,val color: Color)

sealed class OSType{
    data object L90:OSType()
    data object L120:OSType()
    data object L180:OSType()
    data object G180:OSType()
}

data class BarChartDataItem(val barType: BarType,val name: String,val value: Double) {
    @Composable
    fun getColor(): Brush =
        Brush.horizontalGradient(
            colors = when (barType) {
                is BarType.MPlan, BarType.YPlan ->
                    listOf(
                        getColors().chart_m_plan_start,
                        getColors().chart_m_plan_end
                    )
                is BarType.MActual ->
                    listOf(
                        getColors().chart_m_actual_start,
                        getColors().chart_m_actual_end
                    )
                is BarType.YActual ->
                    listOf(
                        getColors().chart_y_actual_start,
                        getColors().chart_y_actual_end
                    )
            }
        )
}
sealed class BarType{
    data object MPlan:BarType()
    data object MActual:BarType()
    data object YPlan:BarType()
    data object YActual:BarType()
}