package com.juagri.shared.domain.model.dashboard

import AppTypography
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.juagri.shared.utils.getColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.juagri.shared.domain.model.focusProduct.FocusProductItem
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.RowSpaceSmall
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.utils.getIndianCurrencyFormat
import com.juagri.shared.utils.theme.chart_m_actual_end
import com.juagri.shared.utils.theme.chart_m_actual_start
import com.juagri.shared.utils.theme.chart_m_plan_end
import com.juagri.shared.utils.theme.chart_m_plan_start
import com.juagri.shared.utils.theme.chart_y_actual_end
import com.juagri.shared.utils.theme.chart_y_actual_start
import com.juagri.shared.utils.theme.getBarColor
import com.juagri.shared.utils.value

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

@Composable
fun FocusProductChartLayout(
    itemsList: List<FocusProductItem>
) {
    if (itemsList.isNotEmpty()) {
        Column {
            itemsList.forEach { barItem ->
                FocusBarItem(barItem)
            }
        }
    }
}

@Composable
private fun FocusBarItem(focusItem: FocusProductItem){
    CardLayout {
        Text(
            focusItem.name.value(),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        ColumnSpaceSmall()
        Row(verticalAlignment = Alignment.CenterVertically) {
            var size by remember { mutableStateOf(IntSize.Zero) }
            Row(modifier = Modifier.weight(1f).fillMaxWidth()
                .onSizeChanged { size = it }) {
                Column {
                    Box {
                        Spacer(
                            modifier = Modifier.fillMaxWidth(1f)
                                .height(20.dp)
                                .background(getColors().getBarColor(BarType.MPlan))
                        )
                        var barPer = 0.0
                        if(focusItem.mActual.value() > 0){
                            barPer = (focusItem.mActual.value() / focusItem.mPlan.value())
                        }
                        Spacer(
                            modifier = Modifier.fillMaxWidth(barPer.toFloat())
                                .height(20.dp)
                                .background(getColors().getBarColor(BarType.MActual))
                        )
                    }
                    ColumnSpaceSmall()
                    Box {
                        Spacer(
                            modifier = Modifier.fillMaxWidth(1f)
                                .height(20.dp)
                                .background(getColors().getBarColor(BarType.YPlan))
                        )
                        var barPer = 0.0
                        if(focusItem.yActual.value() > 0){
                            barPer = (focusItem.yActual.value() / focusItem.yPlan.value())
                        }
                        Spacer(
                            modifier = Modifier.fillMaxWidth(barPer.toFloat())
                                .height(20.dp)
                                .background(getColors().getBarColor(BarType.YActual))
                        )
                    }
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            Row(modifier = Modifier.weight(1f)) {
                Spacer(
                    modifier = Modifier.width(16.dp).height(16.dp)
                        .background(getColors().getBarColor(BarType.MPlan))
                )
                RowSpaceSmall()
                Text(
                    getIndianCurrencyFormat(focusItem.mPlan.toString()),
                    style = AppTypography.titleSmall,
                    color = getColors().onBackground
                )
            }
            Row(modifier = Modifier.weight(1f)) {
                Spacer(
                    modifier = Modifier.width(16.dp).height(16.dp)
                        .background(getColors().getBarColor(BarType.MActual))
                )
                RowSpaceSmall()
                Text(
                    getIndianCurrencyFormat(focusItem.mActual.toString()),
                    style = AppTypography.titleSmall,
                    color = getColors().onBackground
                )
            }
            Row(modifier = Modifier.weight(1f)) {
                Spacer(
                    modifier = Modifier.width(16.dp).height(16.dp)
                        .background(getColors().getBarColor(BarType.YPlan))
                )
                RowSpaceSmall()
                Text(
                    getIndianCurrencyFormat(focusItem.yPlan.toString()),
                    style = AppTypography.titleSmall,
                    color = getColors().onBackground
                )
            }
            Row(modifier = Modifier.weight(1f)) {
                Spacer(
                    modifier = Modifier.width(16.dp).height(16.dp)
                        .background(getColors().getBarColor(BarType.YActual))
                )
                RowSpaceSmall()
                Text(
                    getIndianCurrencyFormat(focusItem.yActual.toString()),
                    style = AppTypography.titleSmall,
                    color = getColors().onBackground
                )
            }
        }
    }
    ColumnSpaceSmall()
}