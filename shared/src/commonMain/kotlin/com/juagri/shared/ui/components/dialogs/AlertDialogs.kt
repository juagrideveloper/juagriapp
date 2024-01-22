package com.juagri.shared.com.juagri.shared.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.RectangleShape
import com.juagri.shared.ui.components.fields.ButtonNormal
import com.juagri.shared.ui.components.fields.TextMedium
import com.juagri.shared.ui.components.fields.TextTitle

@Composable
fun ConfirmDialog(showingDialog:MutableState<Boolean>,title: String,content: String,onClickYes: () -> Unit,onClickNo: () -> Unit) {
    if (showingDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showingDialog.value = false
            },
            text = {
                TextMedium(text = content)
            },
            title = {
                TextTitle(text = title)
            },
            confirmButton = {
                ButtonNormal("Yes"){
                    showingDialog.value = false
                    onClickYes.invoke()
                }
            },
            dismissButton = {
                ButtonNormal("No"){
                    showingDialog.value = false
                    onClickNo.invoke()
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            shape = RectangleShape
        )
    }
}