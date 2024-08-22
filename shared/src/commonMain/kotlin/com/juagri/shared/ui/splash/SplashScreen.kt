package com.juagri.shared.ui.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import chaintech.videoplayer.ui.VideoPlayerView
import chaintech.videoplayer.ui.VideoPlayerWithoutControl
import com.juagri.shared.ui.components.fields.ColumnSpaceLarge
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.RowSpaceMedium
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import com.juagri.shared.ui.components.layouts.SplashImageColumn
import com.juagri.shared.utils.getColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

const val LIST_TAG = "list"
const val TOOLBAR_TAG = "toolbar"
const val FAVORITE_TAG = "favorite"
const val BACK_BUTTON_TAG = "back"
const val TITLE_BAR_TAG = "titleBar"
const val DETAILS_TAG = "details"
@OptIn(ExperimentalResourceApi::class)
@Composable
fun SplashScreen(
    onNext: (Boolean) -> Unit,
) {
    val viewModel = koinViewModel(SplashViewModel::class)
    //viewModel.setDemoUser()
    ScreenLayoutWithoutActionBar {
        var image1 by remember { mutableStateOf(false) }
        var image2 by remember { mutableStateOf(false) }
        var image3 by remember { mutableStateOf(false) }
        var image4 by remember { mutableStateOf(false) }
        var image5 by remember { mutableStateOf(false) }
        var image6 by remember { mutableStateOf(false) }
        var image7 by remember { mutableStateOf(false) }
        var image8 by remember { mutableStateOf(false) }
        var isReadyToNavigate by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                Row(modifier = Modifier.weight(1f)) {
                    Column {
                        ColumnSpaceSmall()
                        var isPause by remember { mutableStateOf(true) }
                        Box {
                            VideoPlayerWithoutControl(
                                modifier = Modifier.fillMaxSize(),
                                url = "https://firebasestorage.googleapis.com/v0/b/ju-agri-cdo-app.appspot.com/o/SplashVideo%2Fjulogovideo.mp4?alt=media",
                                enablePauseResume = false,
                                isPause = isPause,
                                onPauseToggle = { isPause = isPause.not() }
                            )
                            if(isPause) {
                                Column(
                                    modifier = Modifier.fillMaxSize()
                                        .background(getColors().background)
                                ) {}
                            }
                        }
                        AnimatedVisibility(image8, modifier = Modifier.fillMaxWidth()) {
                            isPause = false
                        }
                    }
                }
                RowSpaceMedium()
                Row(modifier = Modifier.weight(1f)) {
                    SplashImageColumn("ic_splash_get_set.png", image7)
                    SplashImageColumn("ic_splash_xpert.png", image6)
                    SplashImageColumn("ic_splash_morgain.png", image5)
                }
                RowSpaceMedium()
                Row(modifier = Modifier.weight(1f)) {
                    SplashImageColumn(visible = false)
                    SplashImageColumn("ic_splash_ecomax.png", image4)
                    SplashImageColumn(visible = false)
                }
                RowSpaceMedium()
                Row(modifier = Modifier.weight(1f)) {
                    SplashImageColumn("ic_splash_rahat.png", image3)
                    SplashImageColumn("ic_splash_vitalgold.png", image2)
                    SplashImageColumn("ic_splash_potash.png", image1)
                }
            }
        }
        val coroutineScope = rememberCoroutineScope()
        coroutineScope.launch {
            for (i in 0..7) {
                delay(500)
                when (i) {
                    0 -> image1 = true
                    1 -> image2 = true
                    2 -> image3 = true
                    3 -> image4 = true
                    4 -> image5 = true
                    5 -> image6 = true
                    6 -> image7 = true
                    7 -> image8 = true
                }
            }
            delay(1500)
        }
        LaunchedEffect(Unit) {
            delay((500 * 8) + 2400 ) // Do some heavy lifting
            isReadyToNavigate = true
        }
        if(isReadyToNavigate){
            onNext.invoke(viewModel.isAlreadyLoggedIn())
        }
    }
}
