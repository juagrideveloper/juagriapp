package com.juagri.shared.ui.notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.router.stack.push
import com.juagri.shared.domain.model.notification.FileType
import com.juagri.shared.domain.model.notification.NotificationAttachment
import com.juagri.shared.domain.model.notification.NotificationItem
import com.juagri.shared.ui.components.fields.ColumnSpaceMedium
import com.juagri.shared.ui.components.fields.LabelTimeStamp
import com.juagri.shared.ui.components.fields.NotificationContent
import com.juagri.shared.ui.components.fields.NotificationTitle
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.ui.components.layouts.getNotificationFileUrl
import com.juagri.shared.ui.navigation.AppScreens
import com.juagri.shared.utils.Constants
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.extension
import com.juagri.shared.utils.startTime
import com.juagri.shared.utils.toDisplayTimeStamp
import com.preat.peekaboo.image.picker.toImageBitmap
import io.github.xxfast.decompose.router.Router
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun NotificationListScreen(
    router: Router<AppScreens>,
    notificationCount: MutableState<Int>
) {
    val viewModel = koinViewModel(NotificationViewModel::class)
    viewModel.setScreenId(Constants.SCREEN_NOTIFICATION_LIST)
    ScreenLayout(viewModel, false) {
        CardLayout(fullHeight = true) {
            when (val result = viewModel.notificationItems.collectAsState().value) {
                is UIState.Success -> {
                    LazyColumn {
                        items(result.data.size) {
                            val item = result.data[it]
                            NotificationListItem(item) {
                                if (item.unread == 0) {
                                    viewModel.updateNotificationStatus(item.id, notificationCount)
                                }
                                router.push(
                                    AppScreens.NotificationDetails(
                                        item.id,
                                        item.regCode,
                                        item.regName,
                                        item.roleId,
                                        item.roleName,
                                        item.title,
                                        item.content,
                                        item.filename,
                                        item.unread,
                                        item.updatedTime.startTime()
                                    )
                                )
                            }
                            ColumnSpaceMedium()
                        }
                    }
                }

                else -> {}
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getNotifications()
    }
}

@Composable
fun NotificationListItem(
    item: NotificationItem,
    selectedAttachment: NotificationAttachment<Any>? = null,
    onClick: (() -> Unit)? = null
) {
    CardLayout {
        Row(
            modifier = Modifier.fillMaxWidth().clickable {
                onClick?.invoke()
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (selectedAttachment != null) {
                when (selectedAttachment.filename.extension()) {
                    FileType.pdf.name -> ListIcon("ic_pdf.xml")
                    FileType.txt.name -> ListIcon("ic_text.xml")
                    FileType.mp4.name -> ListIcon("ic_video.xml")
                    FileType.jpg.name, FileType.jpeg.name, FileType.png.name ->
                        Image(
                            bitmap = (selectedAttachment.content as ByteArray).toImageBitmap(),
                            contentDescription = "Selected Image",
                            modifier =
                            Modifier
                                .size(60.dp)
                                .clip(shape = RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop,
                        )
                }

            } else if (item.filename.isNotEmpty()) {
                when (item.filename.extension()) {
                    FileType.pdf.name -> ListIcon("ic_pdf.xml")
                    FileType.txt.name -> ListIcon("ic_text.xml")
                    FileType.mp4.name -> ListIcon("ic_video.xml")
                    FileType.jpg.name, FileType.jpeg.name, FileType.png.name ->
                        KamelImage(
                            resource = asyncPainterResource(
                                data = getNotificationFileUrl(
                                    item.filename
                                )
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(60.dp)
                                .clip(shape = RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop,
                            onLoading = {
                                CircularProgressIndicator(
                                    progress = { it },
                                )
                            },
                            onFailure = {},
                        )
                }

            }
            Column(modifier = Modifier.padding(start = 8.dp)) {
                NotificationTitle(item.title)
                NotificationContent(item.content, maxLines = 3)
            }
        }
        LabelTimeStamp(item.updatedTime.toDisplayTimeStamp())
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ListIcon(iconName: String){
    Image(
        painterResource(DrawableResource(iconName)),
        contentDescription = "Selected Image",
        modifier =
        Modifier
            .size(60.dp)
            .clip(shape = RoundedCornerShape(12.dp)),
        contentScale = ContentScale.Crop,
    )
}