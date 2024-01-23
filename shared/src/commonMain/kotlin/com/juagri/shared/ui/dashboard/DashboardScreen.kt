package com.juagri.shared.ui.dashboard

import Constants
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.juagri.shared.ui.components.fields.SpaceLarge
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.line.AreaBaseline
import io.github.koalaplot.core.line.AreaPlot
import io.github.koalaplot.core.line.LinePlot
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.style.AreaStyle
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.toString
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.DefaultPoint
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.koalaplot.core.xygraph.rememberLinearAxisModel
import moe.tlaster.precompose.koin.koinViewModel
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random


@Composable
fun DashboardScreen() {
    val viewModel = koinViewModel(DashboardViewModel::class)
    ScreenLayoutWithoutActionBar(modifier=Modifier.background(Color.LightGray)) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            SpaceLarge()
            when(viewModel.getRoleID()) {
                Constants.EMP_ROLE_SO -> barChart()
                Constants.EMP_ROLE_CDO -> pieChart()
                Constants.EMP_ROLE_DL -> areaChart()
            }
        }
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun barChart(){
    Box(modifier = Modifier.height(200.dp)) {
        val boroughs = listOf("A", "B", "C", "D", "E")
        val population = listOf(1.446788f, 2.648452f, 1.638281f, 2.330295f, 0.487155f)
        XYGraph(
            xAxisModel = remember { CategoryAxisModel(boroughs) },
            yAxisModel = rememberLinearAxisModel(0f..3f, minorTickCount = 0),
            yAxisTitle = "Population (Millions)"
        ) {
            VerticalBarPlot(
                xData = boroughs,
                yData = population,
                bar = {
                    DefaultVerticalBar(SolidColor(Color.Blue))
                }
            )
        }
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun pieChart(){
    Box(modifier = Modifier.height(200.dp)) {
        val random = Random(10)
        val data: List<Float> = buildList {
            for (i in 1..10) {
                add(random.nextFloat() * 10f)
            }
        }


        PieChart(
            data,
            label = { i ->
                Text(data[i].toString())
            },
            holeSize = 0.75F,
            holeContent = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column() {
                        Text("Total", style = MaterialTheme.typography.titleLarge)
                        Text(
                            ((data.sum() * 100F).roundToInt() / 100F).toString(),
                            style = MaterialTheme.typography.displaySmall
                        )
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun areaChart(){
    Box(modifier = Modifier.height(200.dp)) {
        val data1 = buildList {
            for (i in 1..10) {
                add(DefaultPoint(i.toFloat(), 10f * (1.04).pow(i).toFloat()))
            }
        }
        val data2 = buildList {
            for (i in 1..10) {
                add(DefaultPoint(i.toFloat(), 10f * (1.06).pow(i).toFloat()))
            }
        }

        XYGraph(
            rememberLinearAxisModel(0f..12f),
            rememberLinearAxisModel(0f..20f),
            xAxisTitle = "",
            yAxisTitle = "",
            xAxisLabels = { it.toString(1) },
            yAxisLabels = { it.toString(1) }
        ) {
            LinePlot(
                data = data1,
                lineStyle = LineStyle(brush = SolidColor(Color(0xFF00498F)), strokeWidth = 2.dp),
            )
            AreaPlot(
                data = data2,
                lineStyle = LineStyle(brush = SolidColor(Color(0xFF37A78F)), strokeWidth = 2.dp),
                areaStyle = AreaStyle(
                    brush = SolidColor(Color(0xFF37A78F)),
                    alpha = 0.5f,
                ),
                areaBaseline = AreaBaseline.ArbitraryLine(data1)
            )
        }
    }
}