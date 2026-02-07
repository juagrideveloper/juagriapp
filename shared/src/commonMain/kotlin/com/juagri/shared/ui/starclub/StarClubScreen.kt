package com.juagri.shared.ui.starclub

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.juagri.shared.ui.components.fields.PromotionHeading
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun StarClubScreen() {
    val viewModel = koinViewModel<StarClubViewModel>()
    LaunchedEffect(Unit) { viewModel.initScreen() }

    val wonTrips = listOf(
        "img_star_club_bronze.png",
        "img_star_club_silver.png",
    )
    val canWinTrips = listOf(
        "img_star_club_gold.png",
        "img_star_club_platinum.png",
        "img_star_club_diamond.png",
    )

    ScreenLayoutWithoutActionBar {
        ScreenLayout(viewModel = viewModel) {
            CardLayout(fullHeight = true, isScrollable = true) {
                Spacer(modifier = Modifier.height(8.dp))
                SectionTitle("You won a Trip !!!")
                Spacer(modifier = Modifier.height(12.dp))
                wonTrips.forEach { trip ->
                    Image(
                        painterResource(DrawableResource(trip)),
                        null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))
                SectionTitle("You can win a Trip !!!")
                Spacer(modifier = Modifier.height(12.dp))
                canWinTrips.forEach { trip ->
                    Image(
                        painterResource(DrawableResource(trip)),
                        null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
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