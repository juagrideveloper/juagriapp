package ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import components.layouts.ScreenLayoutWithoutActionBar
import components.layouts.SplashImageColumn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    onBack: (Int) -> Unit,
) {
    /*ScreenLayoutWithActionBar("Splash"){
        ButtonSmall("Go Back", onClicked = {  onBack.invoke(1)})
        Button(onClick = { onBack.invoke(2) }) {
            Text("Start loading")
        }
    }*/
    var data by remember { mutableStateOf(mutableListOf<Int>()) }
    val columnPadding = 16.dp
    val imageHeight = 150.dp
    ScreenLayoutWithoutActionBar ("Splash"){
        val coroutineScope = rememberCoroutineScope()
        /*Row {
            data.forEach {
                SplashImageColumn()
            }
        }
        coroutineScope.launch {
            for (i in 1..10) {
                delay(1000)
                data.add(i)
            }
        }*/
        BoxWithLayout {
            Row(Modifier.weight(0.25f)) {
                Column(
                    modifier = Modifier.weight(1f).background(Color.Blue),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        painterResource("compose-multiplatform.xml"),
                        null,
                        modifier = Modifier.height(imageHeight)
                    )
                }
                Column(
                    Modifier.weight(1f).background(Color.Yellow)
                ) {
                    Image(
                        painterResource("compose-multiplatform.xml"),
                        null,
                        modifier = Modifier.height(imageHeight)
                    )
                }
                Column(
                    Modifier.weight(1f).background(Color.Yellow)
                ) {
                    Image(
                        painterResource("compose-multiplatform.xml"),
                        null,
                        modifier = Modifier.height(imageHeight)
                    )
                }
            }
        }


    /*LazyHorizontalGrid(
            rows = GridCells.Adaptive(100.dp),
            modifier = Modifier
                .padding(columnPadding)
                .testTag(DETAILS_TAG),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp),
        ) {
            items(8) { i ->
                Image(
                    painterResource("ic_splash_get_set.png"),
                    null,
                )
            }
        }*/
    /*Row{
           Column {
               Image(
                   painterResource("ic_splash_get_set.png"),
                   null,
               )
           }
            Column {
                Image(
                    painterResource("ic_splash_xpert.png"),
                    null,
                )
            }
            Column {
                Image(
                    painterResource("ic_splash_morgain.png"),
                    null,
                )
            }
        }*/
        /*Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painterResource("bg_splash_round.png"),
                null,
                modifier = Modifier.align(Alignment.TopStart).offset(x = -columnPadding,y = -columnPadding)
            )
            Image(
                painterResource("bg_splash_round.png"),
                null,
                modifier = Modifier.align(Alignment.TopEnd).offset(x = -columnPadding,y = -columnPadding)
            )
            Image(
                painterResource("bg_splash_round.png"),
                null,
                modifier = Modifier.align(Alignment.BottomStart).offset(x = -columnPadding,y = -columnPadding)
            )
            Image(
                painterResource("bg_splash_round.png"),
                null,
                modifier = Modifier.align(Alignment.BottomEnd).offset(x = -columnPadding,y = -columnPadding)
            )
        }*/
    }
}

@Composable
fun BoxWithLayout(content: @Composable RowScope.()->Unit){
    Row {
        content()
    }
}
