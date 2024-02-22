package com.juagri.shared.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.juagri.shared.domain.model.filter.FilterItem
import com.juagri.shared.domain.model.filter.FilterType
import com.juagri.shared.ui.components.fields.ButtonNormal
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithActionBar
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import kotlin.math.sqrt

@Composable
fun TestScreen() {
    val viewModel = koinViewModel(TestScreenViewModel::class)
    ScreenLayoutWithActionBar(title = mutableStateOf("Testing")) {
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

        //DealerDashboardScreen()
        /*var size by remember { mutableStateOf(IntSize.Zero) }
        LazyColumn {
            item {
                Column(
                    modifier = Modifier
                        .fillParentMaxHeight(1f)
                        .fillMaxWidth()
                        .background(color = Color.Red)
                        .onSizeChanged { size = it }
                        .padding(8.dp)
                ) {
                    CardLayout(true) {
                        ButtonNormal("Success")
                    }
                }
            }
        }*/
        ButtonNormal("Test"){
            //viewModel.setMenu()
        }
        /*ScreenLayout(viewModel) {
            CardLayout(true) {
                Row {
                    DropDownLayout("Select Region", modifier = Modifier.getModifier().weight(1f)){
                        viewModel.showDialog.value = FilterDataItem("Select Region",getFilterItems("Region",10),mutableStateOf(true))
                    }
                    RowSpaceSmall()
                    DropDownLayout("Select Territory", modifier = Modifier.getModifier().weight(1f)){
                        viewModel.showDialog.value = FilterDataItem("Select Territory",getFilterItems("Territory",5),mutableStateOf(true))
                    }
                }
                ColumnSpaceSmall()
                Row {
                    DropDownLayout("Select Dealer", modifier = Modifier.getModifier()){
                        viewModel.showDialog.value = FilterDataItem("Select Dealer",getFilterItems("Dealer",2),mutableStateOf(true))
                    }
                }
                ColumnSpaceSmall()
                Row {
                    DropDownLayout("Select FinYear", modifier = Modifier.getModifier().weight(1f)){
                        viewModel.showDialog.value = FilterDataItem("Select FinYear",getFilterItems("FinYear"), mutableStateOf(true))
                    }
                    RowSpaceSmall()
                    DropDownLayout("Select Month", modifier = Modifier.getModifier().weight(1f)){
                        viewModel.showDialog.value = FilterDataItem("Select Month",getFilterItems("Month"), mutableStateOf(true))
                    }
                }
                ColumnSpaceSmall()
                FilterDialog(viewModel.showDialog){
                    viewModel.showAlertMessage(it.name)
                }
            }
        }*/
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
        Image(
            painterResource("ic_splash_get_set.png"),
            null,
            modifier = Modifier.height(100.dp).width(100.dp)
        )
    }
}

private fun getFilterItems(title: String,count: Int = 20,data: FilterType): List<FilterItem>{
    val filterItems = mutableListOf<FilterItem>()
    (1..count).forEach {
        filterItems.add(FilterItem("$title $it","$title $it",data))
    }
    return filterItems
}