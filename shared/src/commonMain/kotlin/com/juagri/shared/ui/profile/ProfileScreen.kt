package com.juagri.shared.ui.profile

import Constants
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juagri.shared.ui.components.fields.ColumnSpaceExtraSmall
import com.juagri.shared.ui.components.fields.ColumnSpaceLarge
import com.juagri.shared.ui.components.fields.ColumnSpaceMedium
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.RowSpaceMedium
import com.juagri.shared.ui.components.fields.TextProfileContent
import com.juagri.shared.ui.components.fields.TextProfileHeading
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import com.juagri.shared.ui.components.layouts.SnackbarMessage
import com.juagri.shared.utils.value
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun ProfileScreen() {
    val viewModel = koinViewModel(ProfileViewModel::class)
    viewModel.setDemoUser()
    viewModel.setScreenId(Constants.SCREEN_PROFILE)

    ScreenLayoutWithoutActionBar(modifier= Modifier.background(Color.Yellow)) {
        ScreenLayout(viewModel, false) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(modifier = Modifier.padding(top = 45.dp)) {
                    viewModel.apply {
                        getJUEmployee()?.let {
                            CardLayout {
                                ColumnSpaceLarge()
                                ColumnSpaceMedium()
                                TextProfileHeading(it.name.value())
                                ColumnSpaceSmall()
                                TextProfileContent(it.mobile.value())
                                ColumnSpaceSmall()
                                TextProfileContent(it.role.value())
                                ColumnSpaceSmall()
                                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        TextProfileHeading(names().region)
                                        ColumnSpaceSmall()
                                        TextProfileContent("Trichy")
                                    }
                                    Divider(
                                        color = Color.LightGray,
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .width(0.3.dp)
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        TextProfileHeading(names().territory)
                                        ColumnSpaceSmall()
                                        TextProfileContent("Dharmapuri")
                                    }
                                }
                                ColumnSpaceSmall()
                            }
                            ColumnSpaceSmall()
                            CardLayout {
                                TextProfileHeading("Address")
                                ColumnSpaceSmall()
                                TextProfileContent("SIRIVEL,SIRVELLA,DOOR NO:-10/98, MAIN ROAD,DISTT:- KURNOOL PIN CODE - 518563,ANDHRA PRADESH")
                                ColumnSpaceMedium()
                            }
                            ColumnSpaceSmall()
                            CardLayout {
                                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                                    KamelImage(
                                        resource = asyncPainterResource(data = getProfileImageUrl("6239741466")),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .height(50.dp)
                                            .width(50.dp)
                                            .shadow(elevation = 8.dp, RoundedCornerShape(24.dp)),
                                        contentScale = ContentScale.Crop,
                                        onLoading = { CircularProgressIndicator(it) },
                                        onFailure = {
                                            SnackbarMessage("Error", Color.Red, Color.White)
                                        },
                                    )
                                    RowSpaceMedium()
                                    Column(modifier = Modifier.weight(1f)) {
                                        TextProfileHeading("Regional Manager", textAlign = TextAlign.Start)
                                        ColumnSpaceExtraSmall()
                                        TextProfileContent("Ramesh Kumar", textAlign = TextAlign.Start)
                                        ColumnSpaceExtraSmall()
                                        TextProfileContent("9100100010", textAlign = TextAlign.Start)
                                    }
                                }
                            }
                            ColumnSpaceSmall()
                            CardLayout {
                                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                                    KamelImage(
                                        resource = asyncPainterResource(data = getProfileImageUrl("6260330318")),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .height(50.dp)
                                            .width(50.dp)
                                            .shadow(elevation = 8.dp, RoundedCornerShape(24.dp)),
                                        contentScale = ContentScale.Crop,
                                        onLoading = { CircularProgressIndicator(it) },
                                        onFailure = {
                                            SnackbarMessage("Error", Color.Red, Color.White)
                                        },
                                    )
                                    RowSpaceMedium()
                                    Column(modifier = Modifier.weight(1f)) {
                                        TextProfileHeading("Sales Officer", textAlign = TextAlign.Start)
                                        ColumnSpaceExtraSmall()
                                        TextProfileContent("Monalisa", textAlign = TextAlign.Start)
                                        ColumnSpaceExtraSmall()
                                        TextProfileContent("9100100010", textAlign = TextAlign.Start)
                                    }
                                }
                            }
                        }
                    }
                }
                KamelImage(
                    resource = asyncPainterResource(data = getProfileImageUrl("7000239287")),
                    contentDescription = null,
                    modifier = Modifier
                        .height(90.dp)
                        .width(90.dp)
                        .shadow(elevation = 8.dp, RoundedCornerShape(45.dp))
                        .background(Color.White, RoundedCornerShape(45.dp))
                        .clip(RoundedCornerShape(8.dp)),
                    //.clickable { onClick?.invoke(item) },
                    contentScale = ContentScale.Crop,
                    onLoading = { CircularProgressIndicator(it) },
                    onFailure = {
                        SnackbarMessage("Error", Color.Red, Color.White)
                    },
                )
            }
        }
    }
}

private fun getProfileImageUrl(mobileNo: String) = "https://firebasestorage.googleapis.com/v0/b/judealer-a7f0e.appspot.com/o/ProfilePhotos%2F$mobileNo.jpg?alt=media"