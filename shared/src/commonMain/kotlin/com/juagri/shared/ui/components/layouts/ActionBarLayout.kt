package com.juagri.shared.ui.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.router.stack.push
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.ui.components.fields.TextTitle
import com.juagri.shared.ui.navigation.AppScreens
import com.juagri.shared.ui.splash.BACK_BUTTON_TAG
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.getScreenHeaderColor
import io.github.xxfast.decompose.router.Router

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionBarLayout(
    title: MutableState<String> = mutableStateOf(""),
    image: ImageVector,
    viewModel: BaseViewModel,
    router: Router<AppScreens>? = null,
    onBackPressed: (() -> Unit)? = null
) {
    var menuExpanded by remember {
        mutableStateOf(false)
    }
    CenterAlignedTopAppBar(
        modifier = Modifier.background(getScreenHeaderColor()),
        title = {
            TextTitle(
                text = title.value,
                color = getColors().background
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier
                    .testTag(BACK_BUTTON_TAG),
                onClick = { onBackPressed?.invoke() }
            ) {
                Icon(image, null, tint = getColors().background)
            }
        },
        actions = {
            if(router != null) {
                val currentScreen = router.stack.value.active.configuration
                when (currentScreen) {
                    AppScreens.NotificationList -> {}
                    is AppScreens.NotificationDetails -> {}
                    else -> {
                        BadgedBox(
                            modifier = Modifier.clickable {
                                router.push(AppScreens.NotificationList)
                            },
                            badge = {
                                if (viewModel.notificationCount.value > 0) {
                                    Badge(
                                        modifier = Modifier.offset(x = (-18).dp, y = 0.dp),
                                        containerColor = Color.Yellow
                                    ) {

                                        Text(
                                            viewModel.notificationCount.value.toString(),
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Red
                                        )
                                    }
                                }
                            }) {
                            Icon(
                                Icons.Filled.Notifications,
                                tint = getColors().background,
                                contentDescription = "Notifications"
                            )
                        }
                    }
                }
            }
            /*IconButton(onClick = { menuExpanded = !menuExpanded }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = null,
                    tint = getColors().background
                )
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
            ) {
                DropdownMenuItem(
                    text = {
                        Text("English")
                    },
                    onClick = {
                        viewModel.changeLanguage(AppLanguage.English)
                        menuExpanded = false
                    },
                )
                DropdownMenuItem(
                    text = {
                        Text("Tamil")
                    },
                    onClick = {
                        viewModel.changeLanguage(AppLanguage.Tamil)
                        menuExpanded = false
                    },
                )
                DropdownMenuItem(
                    text = {
                        Text("Hindi")
                    },
                    onClick = {
                        viewModel.changeLanguage(AppLanguage.Hindi)
                        menuExpanded = false
                    },
                )
            }*/
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}