package com.juagri.shared.ui.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.ui.components.dialogs.ProgressDialog
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.getScreenHeaderColor
import com.juagri.shared.utils.theme.screenBackground

data class MessageData(val enable: Boolean = false, val message: String = "")

@Composable
fun ScreenLayout(
    viewModel: BaseViewModel,
    isScrollable: Boolean = false,
    enableBGColor: Boolean = true,
    content: @Composable() () -> Unit
) {
    Box(
        modifier = Modifier.background(color = getColors().screenBackground()),
        contentAlignment = Alignment.TopCenter
    ) {
        if (enableBGColor) {
            Box(
                modifier = Modifier.fillMaxWidth().height(70.dp).background(getScreenHeaderColor())
            ) {

            }
        }
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            val screenModifier = if(isScrollable) {
                Modifier.verticalScroll(rememberScrollState())
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                    .matchParentSize()
            }
            else{
                Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                    .matchParentSize()
            }
            Column(
                modifier = screenModifier
            ) {
                content.invoke()
            }
            viewModel.apply {
                ProgressDialog(z0001)
                z0002.value.let {
                    if (it.enable) {
                        SnackbarMessage(it.message, getColors().primary, Color.White)
                    }
                }
                z0003.value.let {
                    if (it.enable) {
                        SnackbarMessage(
                            it.message,
                            getColors().error,
                            getColors().errorContainer
                        )
                    }
                }
                z0004.value.let {
                    if (it.enable) {
                        SnackbarMessage(
                            it.message,
                            getColors().onTertiaryContainer,
                            getColors().tertiaryContainer
                        )
                    }
                }
                z0005.value.let {
                    if (it.enable) {
                        SnackbarMessage(
                            it.message,
                            getColors().tertiaryContainer,
                            getColors().tertiary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardLayout(fullHeight: Boolean = false,isScrollable: Boolean = false, content: @Composable() () -> Unit) {
    if(fullHeight) {
        var size by remember { mutableStateOf(IntSize.Zero) }
        LazyColumn {
            item {
                Column(
                    modifier = Modifier
                        .fillParentMaxHeight(1f)
                        .fillMaxWidth()
                        .onSizeChanged { size = it }
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = getColors().background,
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        var modifier = Modifier.padding(8.dp).fillMaxHeight()
                        if(isScrollable){
                            modifier = Modifier.padding(8.dp).fillMaxHeight().verticalScroll(rememberScrollState())
                        }
                        Column(modifier = modifier) {
                            content.invoke()
                        }
                    }
                }
            }
        }
    }else {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = getColors().background,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier.padding(8.dp).wrapContentHeight()) {
                content.invoke()
            }
        }
    }
}