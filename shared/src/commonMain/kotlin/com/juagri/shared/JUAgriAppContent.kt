package com.juagri.shared

import androidx.compose.runtime.Composable
import com.juagri.shared.ui.TestScreen
import com.juagri.shared.ui.chat.ChatScreen
import com.juagri.shared.ui.navigation.AppInitNav
import com.juagri.shared.utils.theme.AppTheme

@Composable
fun JUAgriAppContent() {
    AppTheme {
        AppInitNav()
        //TestScreen()
        //ChatScreen {  }
    }
}