package com.juagri.shared.ui.components.layouts

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.juagri.shared.utils.value
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun ProfileImageLayout(phoneNo: String, modifier: Modifier,fromStaffApp: Boolean = false,fromOldDB: Boolean = true) {
    val imageName = remember {
        mutableStateOf(
            if (fromStaffApp) {
                getStaffProfileImageUrl(phoneNo)
            } else {
                getProfileImageUrl(phoneNo, fromOldDB)
            }
        )
    }
    KamelImage(
        resource = asyncPainterResource(data = imageName.value ),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        onLoading = { CircularProgressIndicator(it) },
        onFailure = {
            imageName.value = getProfileImageUrl("0000000000")
        },
    )
}

//private fun getProfileImageUrl(mobileNo: String) = "https://firebasestorage.googleapis.com/v0/b/judealer-a7f0e.appspot.com/o/ProfilePhotos%2F$mobileNo.jpg?alt=media"
private fun getProfileImageUrl(mobileNo: String, fromOldDB: Boolean = false) =
    if (fromOldDB) "https://firebasestorage.googleapis.com/v0/b/jucdo-53500.appspot.com/o/ProfilePhotos%2F$mobileNo.jpg?alt=media" else "https://firebasestorage.googleapis.com/v0/b/jucdo-53500.appspot.com/o/ProfilePhotos%2F$mobileNo.jpg?alt=media"

private fun getStaffProfileImageUrl(mobileNo: String) = "https://firebasestorage.googleapis.com/v0/b/justaff-950e6.appspot.com/o/ProfilePhotos%2F$mobileNo.jpg?alt=media"

@Composable
fun PromotionImage(filename: String){
    KamelImage(
        resource = asyncPainterResource(data = getPromotionImageUrl(filename) ),
        contentDescription = null,
        modifier = Modifier.width(50.dp).height(50.dp),
        contentScale = ContentScale.Crop,
        onLoading = { CircularProgressIndicator(it) },
        onFailure = {},
    )
}

private fun getPromotionImageUrl(filename: String):String =
    "https://firebasestorage.googleapis.com/v0/b/ju-agri-cdo-app.appspot.com/o/PromotionEntryImages%2F$filename?alt=media"
