package com.juagri.shared.ui.notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juagri.shared.domain.model.filter.FilterType
import com.juagri.shared.domain.model.notification.AttachmentType
import com.juagri.shared.domain.model.notification.FileType
import com.juagri.shared.domain.model.notification.NotificationAttachment
import com.juagri.shared.domain.model.notification.NotificationItem
import com.juagri.shared.ui.components.dialogs.FilterDialog
import com.juagri.shared.ui.components.fields.ButtonFullWidth
import com.juagri.shared.ui.components.fields.ColumnSpaceLarge
import com.juagri.shared.ui.components.fields.ColumnSpaceMedium
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.LabelHeading
import com.juagri.shared.ui.components.fields.NotificationContent
import com.juagri.shared.ui.components.fields.NotificationHeading
import com.juagri.shared.ui.components.fields.NotificationTitle
import com.juagri.shared.ui.components.fields.TextDropdown
import com.juagri.shared.ui.components.layouts.AttachmentIcon
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.DropDownLayout
import com.juagri.shared.ui.components.layouts.EdittextLayout
import com.juagri.shared.ui.components.layouts.PeekabooCameraView
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.utils.Constants
import com.juagri.shared.utils.PermissionUtils
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.value
import com.preat.peekaboo.image.picker.toImageBitmap
import dev.gitlive.firebase.firestore.Timestamp
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import io.github.vinceglb.filekit.core.baseName
import io.github.vinceglb.filekit.core.extension
import kotlinx.coroutines.launch
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class, ExperimentalLayoutApi::class)
@Composable
fun SendNotificationScreen() {
    val viewModel = koinViewModel(NotificationViewModel::class)
    viewModel.setScreenId(Constants.SCREEN_SEND_NOTIFICATION)
    val scope = rememberCoroutineScope()
    val selectedAttachment = remember { mutableStateOf<NotificationAttachment<Any>?>(null) }
    // Pick files from Compose
    val launcher = rememberFilePickerLauncher(
        type = PickerType.File(
            extensions = listOf(
                FileType.mp4.name,
                FileType.pdf.name,
                FileType.jpg.name,
                FileType.jpeg.name,
                FileType.png.name,
                FileType.txt.name
            )
        )
    ) { result ->
        scope.launch {
            result?.let { file ->
                selectedAttachment.value = when (file.extension) {
                    FileType.pdf.name ->
                        NotificationAttachment(file.readBytes(), file.name, AttachmentType.PDF)

                    FileType.txt.name ->
                        NotificationAttachment(file.readBytes(), file.name, AttachmentType.TXT)

                    FileType.mp4.name ->
                        NotificationAttachment(file.readBytes(), file.name, AttachmentType.VIDEO)

                    FileType.jpg.name, FileType.jpeg.name, FileType.png.name ->
                        NotificationAttachment(file.readBytes(), file.name, AttachmentType.IMAGE)
                    else -> null
                }
                println("Selected File name: " + file.name)
                println("Selected File baseName: " + file.baseName)
                println("Selected File extension: " + file.extension)
                println("Selected File path: " + file.path)
            }
        }
    }
    val openCameraLauncher = remember { mutableStateOf(false) }
    val openPermissionLauncher = remember { mutableStateOf(false) }
    if (openPermissionLauncher.value) {
        openPermissionLauncher.value = false
        PermissionUtils.CameraPermission {
            if (it) {
                openCameraLauncher.value = true
            } else {
                viewModel.showErrorMessage("Please allow permission in settings.")
            }
        }
    }
    PeekabooCameraView(
        modifier = Modifier.fillMaxSize(),
        openCameraLauncher,
        onCapture = { byteArray ->
            byteArray?.let {
                selectedAttachment.value =
                    NotificationAttachment(it, "captured-image.jpg", AttachmentType.IMAGE)
            }
            openCameraLauncher.value = false
        },
    )
    val notificationTitle = remember { mutableStateOf("") }
    val notificationContent = remember { mutableStateOf("") }
    val readyToSend = remember {
        mutableStateOf(false)
    }
    readyToSend.value = notificationTitle.value != "" && notificationContent.value != ""
    when (val result = viewModel.sendNotificationItem.collectAsState().value) {
        is UIState.Success -> {
            if (result.data) {
                viewModel.selectedRegion.value = null
                viewModel.selectedRole.value = null
                notificationTitle.value = ""
                notificationContent.value = ""
                selectedAttachment.value = null
                viewModel.reset()
            }
        }

        else -> {}
    }
    ScreenLayout(viewModel, false) {
        CardLayout(true, isScrollable = true) {
            DropDownLayout(
                viewModel.names().region,
                mutableStateOf(viewModel.getRegionLabel())
            ) {
                viewModel.getRegionList()
            }
            ColumnSpaceSmall()
            DropDownLayout(
                viewModel.names().role,
                mutableStateOf(viewModel.getRoleLabel())
            ) {
                viewModel.getRoles()
            }
            if (viewModel.selectedRegion.value != null && viewModel.selectedRole.value != null) {
                ColumnSpaceSmall()
                EdittextLayout(
                    viewModel.names().title,
                    notificationTitle,
                )
                OutlinedTextField(
                    value = notificationContent.value,
                    onValueChange = { notificationContent.value = it },
                    label = { TextDropdown(viewModel.names().content) },
                    modifier = Modifier.fillMaxWidth().height(120.dp).padding(0.dp)
                )
                ColumnSpaceSmall()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)// Making it a square
                        .border(0.5.dp, Color.Black, RoundedCornerShape(8.dp)) // Square border
                        .padding(8.dp) // Padding inside the border
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LabelHeading(
                            text = "Select File",
                            modifier = Modifier.padding(start = 8.dp)
                        )

                        Row {
                            Image(
                                painter = painterResource(DrawableResource("icon_camera.xml")),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(50.dp) // Adjust image size
                                    .padding(end = 8.dp)
                                    .clickable {
                                        openPermissionLauncher.value = true
                                    }
                            )
                            Image(
                                painter = painterResource(DrawableResource("ic_attachment_plus.xml")),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(50.dp) // Adjust image size
                                    .padding(end = 8.dp)
                                    .clickable {
                                        launcher.launch()
                                    }
                            )
                        }
                    }
                }
                ColumnSpaceSmall()
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    when (selectedAttachment.value?.attachmentType) {
                        AttachmentType.PDF -> {
                            AttachmentIcon("ic_pdf.xml"){
                                selectedAttachment.value = null
                            }
                        }
                        AttachmentType.IMAGE -> {
                            Box(contentAlignment = Alignment.TopEnd) {
                                Image(
                                    bitmap = (selectedAttachment.value?.content as ByteArray).toImageBitmap(),
                                    contentDescription = "Selected Image",
                                    modifier =
                                    Modifier
                                        .size(100.dp).padding(4.dp)
                                        .clip(shape = RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop,
                                )
                                IconButton(onClick = {
                                    selectedAttachment.value = null
                                }, modifier = Modifier.size(24.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        null,
                                        tint = Color.White,
                                        modifier = Modifier.background(Color.Black)
                                            .clip(RoundedCornerShape(10.dp))
                                    )
                                }
                            }
                        }
                        AttachmentType.VIDEO -> {
                            AttachmentIcon("ic_video.xml"){
                                selectedAttachment.value = null
                            }
                        }
                        AttachmentType.TXT -> {
                            AttachmentIcon("ic_text.xml"){
                                selectedAttachment.value = null
                            }
                        }
                        else -> {}
                    }
                }

                ButtonFullWidth("Submit", readyToSend) {
                    viewModel.sendNotification(
                        NotificationItem(
                            regCode = viewModel.selectedRegion.value?.regCode.value(),
                            regName = viewModel.selectedRegion.value?.regName.value(),
                            roleId = viewModel.selectedRole.value?.roleId.value(),
                            roleName = viewModel.selectedRole.value?.roleName.value(),
                            title = notificationTitle.value.value(),
                            content = notificationContent.value.value()
                        ),
                        selectedAttachment.value
                    )
                }
                ColumnSpaceLarge()
                if (notificationTitle.value != "" && notificationContent.value != "") {
                    HorizontalDivider()
                    ColumnSpaceLarge()
                    NotificationHeading("Notification Item Preview", TextAlign.Center)
                    ColumnSpaceMedium()
                    NotificationListItem(
                        NotificationItem(
                            regCode = viewModel.selectedRegion.value?.regCode.value(),
                            regName = viewModel.selectedRegion.value?.regName.value(),
                            roleId = viewModel.selectedRole.value?.roleId.value(),
                            roleName = viewModel.selectedRole.value?.roleName.value(),
                            title = notificationTitle.value.value(),
                            content = notificationContent.value.value(),
                            filename = "",
                            unread = 0,
                            updatedTime = Timestamp.now()
                        ),
                        selectedAttachment.value
                    )

//                    ColumnSpaceLarge()
//                    HorizontalDivider()
//                    ColumnSpaceLarge()
//                    NotificationHeading("Notification Details", TextAlign.Center)
//                    ColumnSpaceMedium()
//                    NotificationTitle(notificationTitle.value, TextAlign.Center)
//                    ColumnSpaceMedium()
//                    NotificationContent(
//                        notificationContent.value,
//                        TextAlign.Center,
//                        maxLines = 3
//                    )
//                    ColumnSpaceMedium()
//                    if (selectedAttachment.value != null) {
//                        Image(
//                            bitmap = (selectedAttachment.value?.content as ByteArray).toImageBitmap(),
//                            contentDescription = "Selected Image",
//                            modifier =
//                            Modifier
//                                .fillMaxWidth()
//                                .clip(shape = RoundedCornerShape(12.dp)),
//                            contentScale = ContentScale.Crop,
//                        )
//                    }
                }
            }
        }
    }
    FilterDialog(viewModel.showDialog) {
        when (val item = it.data) {
            is FilterType.REGION -> {
                viewModel.selectedRegion.value = item.data
            }

            is FilterType.ROLE -> {
                viewModel.selectedRole.value = item.data
            }

            else -> {}
        }
    }
}