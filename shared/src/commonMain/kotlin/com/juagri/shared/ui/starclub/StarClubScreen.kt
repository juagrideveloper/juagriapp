package com.juagri.shared.ui.starclub

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.juagri.shared.domain.model.starclub.StarClubMetric
import com.juagri.shared.ui.components.fields.PromotionHeading
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import com.juagri.shared.utils.getScreenHeaderColor
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

private data class MetricKey(val displayName: String, val keys: List<String>)

private val METRIC_KEYS = listOf(
    MetricKey("Total Sales", listOf("Total Sales", "TotalSales")),
    MetricKey("Focus Product", listOf("Focus Product", "FocusProduct")),
    MetricKey("DSO", listOf("DSO")),
    MetricKey("Payment by Dec 25", listOf("Payment by Dec 25", "PayDec25")),
    MetricKey("Payment by Jan 26", listOf("Payment by Jan 26", "PayJan26")),
    MetricKey("Payment by Jun 26", listOf("Payment by Jun 26", "PayJun26"))
)

@OptIn(ExperimentalResourceApi::class)
@Composable
fun StarClubScreen() {
    val viewModel = koinViewModel<StarClubViewModel>()
    val state by viewModel.starClubState.collectAsState()
    var showMetricsDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.initScreen() }

    val onImageClick: () -> Unit = {
        showMetricsDialog = true
    }

    ScreenLayoutWithoutActionBar {
        ScreenLayout(viewModel = viewModel) {
            CardLayout(fullHeight = true, isScrollable = true) {
                Spacer(modifier = Modifier.height(8.dp))
                SectionTitle("WON")
                Spacer(modifier = Modifier.height(12.dp))
                state.wonTierImages.forEach { imageName ->
                    Image(
                        painterResource(DrawableResource(imageName)),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onImageClick)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))
                SectionTitle("YOU CAN WIN")
                Spacer(modifier = Modifier.height(12.dp))
                state.canWinTierImages.forEach { imageName ->
                    Image(
                        painterResource(DrawableResource(imageName)),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onImageClick)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    }

    if (showMetricsDialog) {
        StarClubMetricsDialog(
            metrics = state.metrics,
            onDismiss = { showMetricsDialog = false }
        )
    }
}

@Composable
private fun StarClubMetricsDialog(
    metrics: Map<String, StarClubMetric>?,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .widthIn(max = 480.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Gradient header: icon, "Calculation details", X button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(getScreenHeaderColor())
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Calculation details",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }

                // White body: table
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    if (!metrics.isNullOrEmpty()) {
                        CalculationDetailsTable(metrics)
                    } else {
                        Text(
                            text = "No calculation details available.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalculationDetailsTable(metrics: Map<String, StarClubMetric>) {
    val targetWidth = 56.dp
    val valueWidth = 64.dp
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Category",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Target",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End,
                modifier = Modifier.width(targetWidth)
            )
            Text(
                text = "Achieved",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End,
                modifier = Modifier.width(valueWidth)
            )
            Text(
                text = "Shortfall",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.End,
                modifier = Modifier.width(valueWidth)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
            thickness = 1.dp
        )
        Spacer(modifier = Modifier.height(8.dp))
        METRIC_KEYS.forEach { metricKey ->
            val m = metricKey.keys.firstNotNullOfOrNull { metrics[it] }
            if (m != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = metricKey.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = m.target.toInt().toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End,
                        modifier = Modifier.width(targetWidth)
                    )
                    Text(
                        text = m.achieved.toInt().toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End,
                        modifier = Modifier.width(valueWidth)
                    )
                    Text(
                        text = m.shortfall.toInt().toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End,
                        modifier = Modifier.width(valueWidth)
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    PromotionHeading(
        text = text,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}
