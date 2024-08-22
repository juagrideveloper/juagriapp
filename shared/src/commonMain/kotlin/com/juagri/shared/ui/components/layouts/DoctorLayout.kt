package com.juagri.shared.ui.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.juagri.shared.domain.model.doctor.JUDoctorDataItem
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.TextChildTitle
import com.juagri.shared.ui.components.fields.TextCropTitle
import com.juagri.shared.ui.components.fields.TextManagementTitle
import com.juagri.shared.ui.components.fields.TextSolutionNo
import com.juagri.shared.ui.components.fields.TextSolutionTitle
import com.juagri.shared.utils.padZero
import com.juagri.shared.utils.value
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun DoctorCropLayout(item: JUDoctorDataItem, onClick: ((JUDoctorDataItem) -> Unit)? = null){
    Box(
        modifier = Modifier.clickable { onClick?.invoke(item) },
        contentAlignment = Alignment.Center
    ) {
        KamelImage(
            resource = asyncPainterResource(data = getJUDoctorImage(item.image.value())),
            contentDescription = null,
            modifier = Modifier
                .aspectRatio(1F)
                .shadow(elevation = 8.dp, RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(8.dp))
                .clickable { onClick?.invoke(item) },
            contentScale = ContentScale.Crop,
            onLoading = { CircularProgressIndicator(it) },
            onFailure = {
                SnackbarMessage("Error", Color.Red, Color.White)
            },
        )
        //TextCropTitle(item.name.value())
    }
}

@Composable
fun DoctorManagementLayout(item: JUDoctorDataItem,color: Color, onClick: ((JUDoctorDataItem) -> Unit)? = null){
    Box (
        modifier = Modifier.clickable { onClick?.invoke(item) },
        contentAlignment = Alignment.BottomCenter
    ) {
        KamelImage(
            resource = asyncPainterResource(data = getJUDoctorImage(item.image.value().replace(".jpg",".png"))),
            contentDescription = null,
            modifier = Modifier
                .aspectRatio(1F)
                .shadow(elevation = 2.dp, RoundedCornerShape(2.dp))
                .background(color, RoundedCornerShape(2.dp))
                .clip(RoundedCornerShape(2.dp))
                .clickable { onClick?.invoke(item) },
            contentScale = ContentScale.Inside,
            onLoading = { CircularProgressIndicator(it) },
            onFailure = {
                SnackbarMessage("Error", Color.Red, Color.White)
            },
        )
        TextManagementTitle(item.name.value(), modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
    }
}

@Composable
fun DoctorChildLayout(item: JUDoctorDataItem, onClick: ((JUDoctorDataItem) -> Unit)? = null){
    Box(
        modifier = Modifier.clickable { onClick?.invoke(item) },
        contentAlignment = Alignment.Center
    ) {
        KamelImage(
            resource = asyncPainterResource(data = getJUDoctorImage(item.image.value())),
            contentDescription = null,
            modifier = Modifier
                .aspectRatio(1F)
                .shadow(elevation = 2.dp, RoundedCornerShape(2.dp))
                .background(Color.White, RoundedCornerShape(2.dp))
                .clip(RoundedCornerShape(2.dp))
                .clickable { onClick?.invoke(item) },
            contentScale = ContentScale.Crop,
            onLoading = { CircularProgressIndicator(it) },
            onFailure = {
                SnackbarMessage("Error", Color.Red, Color.White)
            },
        )
        TextChildTitle(item.name.value(), modifier = Modifier.fillMaxWidth().fillMaxHeight().background(Color.Black.copy(0.5f)).align(Alignment.BottomCenter).padding(bottom = 16.dp))
    }
}

@Composable
fun DoctorSolutionLayout(ind: Int,item: JUDoctorDataItem, onClick: ((JUDoctorDataItem) -> Unit)? = null) {
    Column(
        modifier = Modifier.padding(top = 12.dp, bottom = 12.dp).clickable { onClick?.invoke(item) },
    ) {
        Row {
            Column(modifier = Modifier.weight(0.5f)) {
                TextSolutionNo(ind.padZero(2))
                ColumnSpaceSmall()
                KamelImage(
                    resource = asyncPainterResource(data = getJUDoctorImage(item.image.value())),
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(1F)
                        .clickable { onClick?.invoke(item) },
                    contentScale = ContentScale.Inside,
                    onLoading = { CircularProgressIndicator(it) },
                    onFailure = {
                        SnackbarMessage("Error", Color.Red, Color.White)
                    },
                )
            }
            TextSolutionTitle(item.name.value(), modifier = Modifier.fillMaxWidth().fillMaxHeight().align(Alignment.CenterVertically).weight(1f))
        }
    }
}

private fun getJUDoctorImage(imageName: String) =
    "https://firebasestorage.googleapis.com/v0/b/judealer-a7f0e.appspot.com/o/JU_Doc_Images%2F$imageName?alt=media"
