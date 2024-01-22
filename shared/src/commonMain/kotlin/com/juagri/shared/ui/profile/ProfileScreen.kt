package com.juagri.shared.com.juagri.shared.ui.profile

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.juagri.shared.ui.components.fields.TextTitle
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar

@Composable
fun ProfileScreen() {
    ScreenLayoutWithoutActionBar(modifier= Modifier.background(Color.Yellow)) {
        TextTitle("Profile")
    }
}