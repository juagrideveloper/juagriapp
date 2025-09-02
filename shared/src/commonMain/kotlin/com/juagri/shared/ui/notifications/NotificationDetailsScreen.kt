package com.juagri.shared.ui.notifications

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juagri.shared.domain.model.notification.FileType
import com.juagri.shared.domain.model.notification.NotificationItem
import com.juagri.shared.ui.components.fields.ColumnSpaceMedium
import com.juagri.shared.ui.components.fields.FilenameCard
import com.juagri.shared.ui.components.fields.NotificationContent
import com.juagri.shared.ui.components.fields.NotificationTitle
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.getNotificationFileUrl
import com.juagri.shared.utils.Constants
import com.juagri.shared.utils.extension
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun NotificationDetailsScreen(notificationItem: NotificationItem) {
    val viewModel = koinViewModel(NotificationViewModel::class)
    viewModel.setScreenId(Constants.SCREEN_NOTIFICATION_DETAILS)
    CardLayout(fullHeight = true, isScrollable = true) {
        ColumnSpaceMedium()
        NotificationTitle(notificationItem.title, TextAlign.Center)
        ColumnSpaceMedium()
        NotificationContent(notificationItem.content, TextAlign.Center)
        ColumnSpaceMedium()
        val uriHandler = LocalUriHandler.current
        when (notificationItem.filename.extension()) {
            FileType.pdf.name, FileType.txt.name, FileType.mp4.name ->
                FilenameCard(notificationItem.filename) {
                    uriHandler.openUri(getNotificationFileUrl(filename = notificationItem.filename))
                }

            FileType.jpg.name, FileType.jpeg.name, FileType.png.name ->
            KamelImage(
                resource = asyncPainterResource(
                    data = getNotificationFileUrl(
                        notificationItem.filename
                    )
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
                    .clip(shape = RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                onLoading = {
                    CircularProgressIndicator(
                        progress = { it },
                    )
                },
                onFailure = {},
            )
            else -> {}
        }
    }
}