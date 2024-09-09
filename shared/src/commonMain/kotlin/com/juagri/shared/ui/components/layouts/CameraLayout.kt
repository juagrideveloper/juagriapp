package com.juagri.shared.ui.components.layouts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.preat.peekaboo.ui.camera.PeekabooCamera
import com.preat.peekaboo.ui.camera.rememberPeekabooCameraState

@Composable
fun PeekabooCameraView(
    modifier: Modifier = Modifier,
    openCamera: MutableState<Boolean>,
    onCapture: (ByteArray?) -> Unit
) {
    if (openCamera.value) {
        Dialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = {}
        ) {
            Surface(modifier = Modifier.fillMaxSize()) {
                val state = rememberPeekabooCameraState(onCapture = {
                    onCapture.invoke(it)
                })
                Box(modifier = modifier) {
                    PeekabooCamera(
                        state = state,
                        modifier = Modifier.fillMaxSize(),
                        permissionDeniedContent = {

                        },
                    )
                    CameraOverlay(
                        isCapturing = state.isCapturing,
                        onBack = { openCamera.value = false },
                        onCapture = {
                            state.capture()
                        },
                        onConvert = { state.toggleCamera() },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}

@Composable
private fun CameraOverlay(
    isCapturing: Boolean,
    onCapture: () -> Unit,
    onConvert: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(32.dp),
    ) {
        IconButton(
            onClick = onBack,
            modifier =
            Modifier
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = IconClose,
                contentDescription = "Back Button",
                tint = Color.White,
            )
        }
        if (isCapturing) {
            CircularProgressIndicator(
                modifier =
                Modifier
                    .size(80.dp)
                    .align(Alignment.Center),
                color = Color.White.copy(alpha = 0.7f),
                strokeWidth = 8.dp,
            )
        }
        CircularButton(
            imageVector = IconCached,
            modifier =
            Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp),
            onClick = onConvert,
        )
        InstagramCameraButton(
            modifier =
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            onClick = onCapture,
        )
    }
}
object PeekabooColors {
    val background = Color(0xFFFFFFFF)
    val onBackground = Color(0xFF19191C)
    val uiLightBlack = Color(25, 25, 28).copy(alpha = 0.7f)
    val darkBackground = Color(0xFF19191C)
    val darkOnBackground = Color(0xFFFFFFFF)
    val primary = Color(0xFFE0E0E0)
    val onPrimary = Color(0xFF000000)
    val darkPrimary = Color(0xFF424242)
    val darkOnPrimary = Color(0xFFFFFFFF)
}

@Composable
fun CircularButton(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(PeekabooColors.uiLightBlack)
            .run {
                if (enabled) {
                    clickable { onClick() }
                } else {
                    this
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
fun CircularButton(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    CircularButton(
        modifier = modifier,
        content = {
            Icon(imageVector, null, Modifier.size(34.dp), Color.White)
        },
        enabled = enabled,
        onClick = onClick,
    )
}

@Composable
internal fun InstagramCameraButton(
    modifier: Modifier = Modifier,
    size: Dp = 70.dp,
    borderSize: Dp = 5.dp,
    onClick: () -> Unit,
) {
    // Outer size including the border
    val outerSize = size + borderSize * 2
    // Inner size excluding the border
    val innerSize = size - borderSize

    Box(
        modifier =
        modifier
            .size(outerSize)
            .clip(CircleShape)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        // Surface for the border effect
        Surface(
            modifier = Modifier.size(outerSize),
            shape = CircleShape,
            color = Color.Transparent,
            border = BorderStroke(borderSize, Color.White),
        ) {}

        // Inner clickable circle
        Box(
            modifier =
            Modifier
                .size(innerSize)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { onClick() },
            contentAlignment = Alignment.Center,
        ) {}
    }
}