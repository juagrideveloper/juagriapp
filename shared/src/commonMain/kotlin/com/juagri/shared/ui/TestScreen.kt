package com.juagri.shared.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import chaintech.videoplayer.ui.VideoPlayerView
import chaintech.videoplayer.ui.VideoPlayerWithoutControl
import com.juagri.shared.domain.model.filter.FilterItem
import com.juagri.shared.domain.model.filter.FilterType
import com.juagri.shared.domain.model.liquidation.DealerLiquidationData
import com.juagri.shared.ui.components.dialogs.SuccessDialog
import com.juagri.shared.ui.components.fields.ButtonNormal
import com.juagri.shared.ui.components.fields.ColumnSpaceMedium
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.DashboardLabelHeading
import com.juagri.shared.ui.components.fields.TextMedium
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithActionBar
import com.juagri.shared.utils.PermissionUtils
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.getColors
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Resource
import org.jetbrains.compose.resources.ResourceItem
import kotlin.math.sqrt


@OptIn(ExperimentalResourceApi::class)
@Composable
fun TestScreen() {
    val viewModel = koinViewModel(TestScreenViewModel::class)
    ScreenLayoutWithActionBar(title = mutableStateOf("Testing"), viewModel = viewModel) {
        ScreenLayout(viewModel) {
           /* VideoPlayerView(
                modifier = Modifier.height(200.dp).padding(16.dp),
                url = "https://firebasestorage.googleapis.com/v0/b/ju-agri-cdo-app.appspot.com/o/SplashVideo%2Fjulogovideo.mp4?alt=media",
                enablePauseResume = true,
            )*/
            var isPause by remember { mutableStateOf(true) }
            VideoPlayerWithoutControl(
                modifier = Modifier.height(200.dp).padding(16.dp),
                url = "https://firebasestorage.googleapis.com/v0/b/ju-agri-cdo-app.appspot.com/o/SplashVideo%2Fjulogovideo.mp4?alt=media",
                enablePauseResume = false,
                isPause = isPause,
                onPauseToggle = {isPause = isPause.not()}
            )
            ButtonNormal("PlayNow"){
                isPause = !isPause
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    shape: Shape = TextFieldDefaults.shape,
    textStyle: TextStyle = LocalTextStyle.current,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    trailingIcon: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    placeHolder: @Composable (() -> Unit)? = null,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        enabled = enabled,
        textStyle = textStyle,
        singleLine = singleLine,
    ) { innerTextField ->

        TextFieldDefaults.DecorationBox(
            value = value,
            visualTransformation = visualTransformation,
            innerTextField = innerTextField,
            singleLine = singleLine,
            enabled = enabled,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(0.dp), // this is how you can remove the padding
            trailingIcon = trailingIcon,
            placeholder = placeHolder,
            leadingIcon = leadingIcon,
            shape = shape,
            colors = colors
        )
    }
}


@Composable
private fun LiquidationRow(title: String,liquidation: MutableState<String>){
    Row(
        modifier = Modifier.wrapContentHeight().border(0.2.dp, getColors().onBackground),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextMedium(
            title,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
                .weight(1f)
        )
        TextField(
            value = liquidation.value,
            onValueChange = {
                liquidation.value = it
            },
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
                .weight(1f)
        )
    }
}



@OptIn(ExperimentalResourceApi::class)
@Composable
private fun RibbonSample() {

    val text = "V2.2.1"
    val textMeasurer = rememberTextMeasurer()
    val style = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White
    )
    val textLayoutResult: TextLayoutResult = remember {
        textMeasurer.measure(text = AnnotatedString(text), style = style)
    }

    Box(
        modifier = Modifier
            .clipToBounds()
            .drawWithContent {

                val canvasWidth = size.width

                val textSize = textLayoutResult.size
                val textWidth = textSize.width
                val textHeight = textSize.height

                val rectWidth = textWidth * 7f
                val rectHeight = textHeight * 1.1f

                val rect = Rect(
                    offset = Offset(canvasWidth - rectWidth, 0f),
                    size = Size(rectWidth, rectHeight)
                )

                val translatePos = sqrt(rectWidth / 2f) * sqrt(rectWidth / 2f)

                drawContent()
                withTransform(
                    {
                        rotate(
                            degrees = 45f,
                            pivot = Offset(
                                canvasWidth - rectWidth / 2,
                                translatePos
                            )
                        )
                    }
                ) {
                    drawRect(
                        Color.Red,
                        topLeft = rect.topLeft,
                        size = rect.size
                    )
                    drawText(
                        textMeasurer = textMeasurer,
                        text = text,
                        style = style,
                        topLeft = Offset(
                            rect.left + (rectWidth - textWidth) / 2f,
                            rect.top + (rect.bottom - textHeight) / 2f
                        )
                    )
                }

            }
    ) {
        /*Image(
            painterResource("ic_splash_get_set.png"),
            null,
            modifier = Modifier.height(100.dp).width(100.dp)
        )*/
    }
}

private fun getFilterItems(title: String,count: Int = 20,data: FilterType): List<FilterItem>{
    val filterItems = mutableListOf<FilterItem>()
    (1..count).forEach {
        filterItems.add(FilterItem("$title $it","$title $it",data))
    }
    return filterItems
}