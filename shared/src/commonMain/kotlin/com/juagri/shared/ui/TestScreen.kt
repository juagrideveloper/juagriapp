package com.juagri.shared.com.juagri.shared.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.juagri.shared.com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.com.juagri.shared.ui.components.layouts.SnackbarMessage
import com.juagri.shared.ui.components.fields.ButtonNormal
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithActionBar
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun TestScreen() {
    val viewModel = koinViewModel(TestScreenViewModel::class)
    ScreenLayoutWithActionBar(title = "Testing") {
        /*KamelImage(
            resource = asyncPainterResource(data = "https://buffer.com/cdn-cgi/image/w=1000,fit=contain,q=90,f=auto/library/content/images/size/w1200/2023/10/free-images.jpg"),
            contentDescription = null,
            modifier = Modifier
                .aspectRatio(1F)
                .padding(8.dp)
                .shadow(elevation = 8.dp, RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop,
            onLoading = { CircularProgressIndicator(it) },
            onFailure = { exception: Throwable ->
                SnackbarMessage("Error",Color.Red,Color.White)
            },
        )*/
        ScreenLayout(viewModel) {->
            ButtonNormal("ShowLoading"){
                viewModel.showProgressBar(true)
            }

            ButtonNormal("Success"){
                viewModel.showSuccessMessage("Success")
            }

            ButtonNormal("Error"){
                viewModel.showErrorMessage("Error")
            }

            ButtonNormal("Alert"){
                viewModel.showAlertMessage("Alert")
            }

            ButtonNormal("Normal"){
                viewModel.showNormalMessage("Normal")
            }
        }
    }
}