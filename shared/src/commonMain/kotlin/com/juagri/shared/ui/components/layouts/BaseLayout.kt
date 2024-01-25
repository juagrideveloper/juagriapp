package com.juagri.shared.com.juagri.shared.ui.components.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.ui.components.dialogs.ProgressDialog

data class MessageData(val enable: Boolean = false,val message: String = "")
@Composable
fun ScreenLayout(viewModel: BaseViewModel, content: @Composable() () -> Unit) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .matchParentSize()
        ) {
            content.invoke()
        }
        viewModel.apply {
            ProgressDialog(z0001)
            z0002.value.let {
                if (it.enable) {
                    SnackbarMessage(it.message,MaterialTheme.colorScheme.primary, Color.White)
                }
            }
            z0003.value.let {
                if (it.enable) {
                    SnackbarMessage(it.message,MaterialTheme.colorScheme.error,MaterialTheme.colorScheme.errorContainer)
                }
            }
            z0004.value.let {
                if (it.enable) {
                    SnackbarMessage(it.message,MaterialTheme.colorScheme.onTertiaryContainer,MaterialTheme.colorScheme.tertiaryContainer)
                }
            }
            z0005.value.let {
                if (it.enable) {
                    SnackbarMessage(it.message, MaterialTheme.colorScheme.tertiaryContainer,MaterialTheme.colorScheme.tertiary)
                }
            }
        }
    }
}