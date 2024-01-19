package com.juagri.shared.com.juagri.shared.ui.components.layouts

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.juagri.shared.com.juagri.shared.ui.components.fields.TextTitle
import com.juagri.shared.ui.splash.BACK_BUTTON_TAG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionBarLayout(title: String = "", onBackPressed: (() -> Unit)? = null) {
    val backColor = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.secondary
        )
    )
    CenterAlignedTopAppBar(
        modifier = Modifier.background(backColor),
        title = {
            TextTitle(
                text = title,
                color = MaterialTheme.colorScheme.background
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .testTag(BACK_BUTTON_TAG),
                onClick = { onBackPressed?.invoke() }
            ) {
                Icon(Icons.Default.Menu, null,tint = MaterialTheme.colorScheme.background)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}