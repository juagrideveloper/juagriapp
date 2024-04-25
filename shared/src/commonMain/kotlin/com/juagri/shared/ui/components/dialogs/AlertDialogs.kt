package com.juagri.shared.ui.components.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.juagri.shared.ui.components.fields.ButtonNormal
import com.juagri.shared.ui.components.fields.ColumnSpaceLarge
import com.juagri.shared.ui.components.fields.ColumnSpaceMedium
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.TextMedium
import com.juagri.shared.ui.components.fields.TextSmall
import com.juagri.shared.ui.components.fields.TextTitle
import com.juagri.shared.ui.components.layouts.IconClose
import com.juagri.shared.ui.components.layouts.ProfileImageLayout
import com.juagri.shared.utils.getColors
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun ConfirmDialog(showConfirmDialog: MutableState<Boolean>, title: String,content: String,onClickYes: () -> Unit) {
    if(showConfirmDialog.value) {
        AlertDialog(
            onDismissRequest = {},
            text = {
                TextMedium(text = content)
            },
            title = {
                TextTitle(text = title)
            },
            confirmButton = {
                ButtonNormal("Yes") {
                    showConfirmDialog.value = false
                    onClickYes.invoke()
                }
            },
            dismissButton = {
                ButtonNormal("No") {
                    showConfirmDialog.value = false
                }
            },
            containerColor = getColors().surfaceVariant,
            shape = RectangleShape
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SuccessDialog(
    showConfirmDialog: MutableState<Boolean>,
    title: String = "",
    desc: String = "",
    onDismiss: () -> Unit
) {
    if(showConfirmDialog.value) {
        Dialog(onDismissRequest = { }) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Spacer(Modifier.height(40.dp))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ColumnSpaceLarge()
                            ColumnSpaceSmall()
                            TextTitle(title.uppercase())
                            ColumnSpaceMedium()
                            TextSmall(desc)
                            ColumnSpaceLarge()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                ButtonNormal("Okay"){
                                    onDismiss.invoke()
                                    showConfirmDialog.value = false
                                }
                            }
                        }
                    }
                }
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "",
                    tint = getColors().primary,
                    modifier = Modifier.align(Alignment.TopCenter)
                        .height(80.dp)
                        .width(80.dp)
                        .background(Color.White, RoundedCornerShape(45.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
            }
        }
    }
}