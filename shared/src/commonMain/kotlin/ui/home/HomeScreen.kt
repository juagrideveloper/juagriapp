package ui.home

import androidx.compose.runtime.Composable
import components.layouts.ScreenLayoutWithActionBar

@Composable
fun HomeScreen(onBack: () -> Unit) {
    ScreenLayoutWithActionBar(title = "Home", onBackPressed = { onBack.invoke() }) {

    }
}