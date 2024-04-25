package com.juagri.shared.ui.profile

import Constants
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import com.juagri.shared.ui.components.layouts.ProfileImageLayout
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import com.juagri.shared.ui.components.layouts.SnackbarMessage
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.value
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun ProfileScreen() {
    val viewModel = koinViewModel(ProfileViewModel::class)
    viewModel.setScreenId(Constants.SCREEN_PROFILE)

    ScreenLayoutWithoutActionBar(modifier = Modifier.background(Color.Yellow)) {
        viewModel.apply {
            getJUEmployee()?.let {
                ScreenLayout(viewModel, true) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(modifier = Modifier.padding(top = 45.dp)) {

                            CardLayout {
                                ColumnSpaceLarge()
                                ColumnSpaceMedium()
                                TextProfileHeading(it.name.value()+" (${getJUEmployee()?.code.value()})")
                                ColumnSpaceSmall()
                                TextProfileContent(it.mobile.value())
                                ColumnSpaceSmall()
                                TextProfileContent(it.role.value())
                                ColumnSpaceSmall()
                                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        TextProfileHeading(names().region)
                                        ColumnSpaceSmall()
                                        TextProfileContent(it.regionList.first().regName.value())
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
                                        TextProfileContent(it.territoryList.first().tName.value())
                                    }
                                }
                                ColumnSpaceSmall()
                            }
                            ColumnSpaceSmall()
                            it.regionList.forEach { region ->
                                CardLayout {
                                    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                                        ProfileImageLayout(
                                            region.rmPhoneNo.value(),
                                            Modifier
                                                .height(50.dp)
                                                .width(50.dp)
                                                .shadow(elevation = 8.dp, RoundedCornerShape(24.dp))
                                            ,
                                            true
                                        )
                                        RowSpaceMedium()
                                        Column(modifier = Modifier.weight(1f)) {
                                            TextProfileHeading(
                                                "Regional Manager - "+region.regName.value(),
                                                textAlign = TextAlign.Start
                                            )
                                            ColumnSpaceExtraSmall()
                                            TextProfileContent(
                                                region.rmName.value(),
                                                textAlign = TextAlign.Start
                                            )
                                            ColumnSpaceExtraSmall()
                                            TextProfileContent(
                                                region.rmPhoneNo.value(),
                                                textAlign = TextAlign.Start
                                            )
                                        }
                                    }
                                }
                                ColumnSpaceSmall()
                                if(getRoleID() == "CDO" || getRoleID() == "SO") {
                                    it.territoryList.filter { terr ->
                                        terr.regCode.value() == region.regCode.value()
                                    }
                                        .forEach { territory ->
                                            CardLayout {
                                                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                                                    ProfileImageLayout(
                                                        territory.soPhoneNo.value(),
                                                        Modifier
                                                            .height(50.dp)
                                                            .width(50.dp)
                                                            .shadow(
                                                                elevation = 8.dp,
                                                                RoundedCornerShape(24.dp)
                                                            ),
                                                        true
                                                    )
                                                    RowSpaceMedium()
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        TextProfileHeading(
                                                            "Sales Officer - " + territory.tName.value(),
                                                            textAlign = TextAlign.Start
                                                        )
                                                        ColumnSpaceExtraSmall()
                                                        TextProfileContent(
                                                            territory.soName.value(),
                                                            textAlign = TextAlign.Start
                                                        )
                                                        ColumnSpaceExtraSmall()
                                                        TextProfileContent(
                                                            territory.soPhoneNo.value(),
                                                            textAlign = TextAlign.Start
                                                        )
                                                    }
                                                }
                                            }
                                            ColumnSpaceSmall()
                                        }
                                }
                            }
                            CardLayout {
                                Row(modifier = Modifier.height(IntrinsicSize.Min).clickable {
                                    viewModel.logout()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(35.dp)
                                            .shadow(elevation = 8.dp, RoundedCornerShape(24.dp))
                                    )
                                    RowSpaceMedium()
                                    Column(modifier = Modifier.weight(1f)) {
                                        TextProfileHeading(
                                            "JU CDO APP",
                                            textAlign = TextAlign.Start
                                        )
                                        ColumnSpaceExtraSmall()
                                        TextProfileContent(
                                            "Sign Out",
                                            textAlign = TextAlign.Start
                                        )
                                    }
                                }
                            }
                        }
                        ProfileImageLayout(
                            it.mobile.value(),
                            Modifier
                                .height(90.dp)
                                .width(90.dp)
                                .shadow(elevation = 8.dp, RoundedCornerShape(45.dp))
                                .background(Color.White, RoundedCornerShape(45.dp))
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
        }
    }
}

private fun getProfileImageUrl(mobileNo: String) = "https://firebasestorage.googleapis.com/v0/b/judealer-a7f0e.appspot.com/o/ProfilePhotos%2F$mobileNo.jpg?alt=media"