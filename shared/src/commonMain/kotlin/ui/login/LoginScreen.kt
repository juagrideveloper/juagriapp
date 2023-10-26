package ui.login

import androidx.compose.runtime.Composable
import components.layouts.ScreenLayoutWithActionBar

@Composable
fun LoginScreen(onBack: () -> Unit) {
    ScreenLayoutWithActionBar(title = "Login", onBackPressed = { onBack.invoke() }) {

    }
}