package ui.login

import androidx.compose.runtime.Composable
import components.layouts.ScreenLayoutWithActionBar
import components.layouts.ScreenLayoutWithoutActionBar

@Composable
fun LoginScreen(onBack: () -> Unit) {
    ScreenLayoutWithActionBar(title = "Login22", onBackPressed = {
        onBack.invoke()
    }) {

    }
    /*ScreenLayoutWithoutActionBar("Login") {
        //ImageCaptureScreen()
    }*/
}