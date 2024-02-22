package com.juagri.shared.ui.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import com.juagri.shared.utils.getColors
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.juagri.shared.domain.model.dashboard.BarChartDataItem
import com.juagri.shared.domain.model.dashboard.BarType
import com.juagri.shared.domain.model.dashboard.DealerSales
import com.juagri.shared.domain.model.dashboard.OSChartItem
import com.juagri.shared.ui.components.fields.ColumnSpaceMedium
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.RowSpaceLarge
import com.juagri.shared.ui.components.fields.RowSpaceMedium
import com.juagri.shared.ui.components.fields.RowSpaceSmall
import com.juagri.shared.ui.components.fields.TextMedium
import com.juagri.shared.ui.components.fields.TextPieLegend
import com.juagri.shared.ui.components.fields.TextSmall
import com.juagri.shared.ui.components.fields.TextTitle
import com.juagri.shared.utils.theme.chart_m_actual_end
import com.juagri.shared.utils.theme.chart_m_actual_start
import com.juagri.shared.utils.theme.chart_y_actual_end
import com.juagri.shared.utils.theme.chart_y_actual_start
import com.juagri.shared.utils.value
import io.github.koalaplot.core.pie.DefaultSlice
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import kotlin.math.roundToInt

@Composable
fun HorizontalBarChartLayout(
    itemsList: List<BarChartDataItem>,onBarClicked:((BarChartDataItem) -> Unit)? =null
) {
    if (itemsList.isNotEmpty()) {
        var size by remember { mutableStateOf(IntSize.Zero) }
        val maxValue = itemsList.maxBy { it.value }.value
        Column {
            itemsList.forEachIndexed { index, barItem ->
                Row {
                    Text(
                        barItem.name,
                        modifier = Modifier.weight(0.4f).wrapContentHeight(),
                        textAlign = TextAlign.End,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1
                    )
                    Spacer(
                        modifier = Modifier.width(5.dp)
                    )
                    Row(modifier = Modifier.weight(1f).fillMaxWidth().onSizeChanged { size = it }) {
                        var barPer = (barItem.value / maxValue) - 0.10
                        if (barPer < 0) {
                            barPer = 0.0
                        }
                        Spacer(
                            modifier = Modifier.fillMaxWidth(barPer.toFloat())
                                .height(20.dp)
                                .background(barItem.getColor())
                                .clickable {
                                    onBarClicked?.invoke(barItem)
                                }
                        )
                        Text(
                            barItem.value.toString(),
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1
                        )
                    }
                }
                RowSpaceSmall()
            }
        }
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun OSChartLayout(osItems: List<OSChartItem>,enableLegend: Boolean = true,onItemClicked:((OSChartItem)->Unit)?=null) {
    Row {
        Box(modifier = Modifier.height(200.dp)) {
            PieChart(
                osItems.map { it.value },
                labelConnector = {},
                holeSize = 0.50F,
                slice = { index ->
                    DefaultSlice(
                        color = osItems[index].color,
                        antiAlias = true
                    )
                },
                holeContent = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            TextMedium("Total")
                            TextSmall(
                                ((osItems.map { it.value }.sum() * 100F).roundToInt() / 100F).toString()
                            )
                        }
                    }
                }
            )
        }
        if(enableLegend) {
            Column(
                modifier = Modifier.fillMaxSize().height(200.dp).padding(start = 20.dp),
                verticalArrangement = Arrangement.Center
            ) {
                osItems.forEach {
                    TextPieLegend(it) { item ->
                        onItemClicked?.invoke(item)
                    }
                    ColumnSpaceSmall()
                }
            }
        }
    }
}

@Composable
fun ProductSalesReport(productSales: List<DealerSales>){
    var checked by remember { mutableStateOf(true) }
    RowSpaceMedium()
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextTitle("Product Sales", modifier = Modifier.weight(1f))
        Row(modifier = Modifier.weight(1f),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
            TextMedium("Month")
            ColumnSpaceMedium()
            Switch(
                modifier = Modifier.height(10.dp),
                checked = checked,
                onCheckedChange = {
                    checked = it
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = getColors().chart_y_actual_start,
                    checkedTrackColor = getColors().chart_y_actual_end,
                    uncheckedThumbColor = getColors().chart_m_actual_start,
                    uncheckedTrackColor = getColors().chart_m_actual_end,
                )
            )
            ColumnSpaceMedium()
            TextMedium("Year")
        }
    }
    RowSpaceLarge()
    if (checked) {
        HorizontalBarChartLayout(productSales.filter { it.type.value() == "YTD" }.map {
            BarChartDataItem(BarType.YActual, it.brand.value(), it.qty.value().toDouble())
        })
    } else {
        HorizontalBarChartLayout(productSales.filter { it.type.value() == "MTD" }.map {
            BarChartDataItem(BarType.MActual, it.brand.value(), it.qty.value().toDouble())
        })
    }
}