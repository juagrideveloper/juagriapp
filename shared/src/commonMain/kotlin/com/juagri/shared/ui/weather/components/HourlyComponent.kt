package com.juagri.shared.ui.weather.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.juagri.shared.ui.components.layouts.SnackbarMessage
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun HourlyComponent(
    modifier: Modifier = Modifier,
    time: String,
    icon: String,
    temperature: String,
) {
    ElevatedCard(
        modifier = modifier.padding(end = 12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = time.substring(11, 13),
                style = MaterialTheme.typography.titleMedium,
            )
            KamelImage(
                modifier = Modifier.size(42.dp),
                resource = asyncPainterResource(data = "https://$icon"),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                onLoading = { CircularProgressIndicator(it) },
                onFailure = {
                    SnackbarMessage("Error", Color.Red, Color.White)
                },
            )
            Text(
                text = temperature,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}