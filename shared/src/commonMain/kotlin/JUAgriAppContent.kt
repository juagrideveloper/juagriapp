package com.juagri.shared

import androidx.compose.runtime.Composable
import com.juagri.shared.ui.navigation.AppInitNav
import com.juagri.shared.ui.theme.AppTheme

@Composable
fun JUAgriAppContent() {
    AppTheme {
        AppInitNav()
    }
}