package com.juagri.shared.ui.weather

import Constants
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import com.juagri.shared.ui.components.layouts.SnackbarMessage
import com.juagri.shared.ui.weather.components.Animation
import com.juagri.shared.ui.weather.components.ForecastComponent
import com.juagri.shared.ui.weather.components.HourlyComponent
import com.juagri.shared.ui.weather.components.SearchWidgetState
import com.juagri.shared.ui.weather.components.WeatherComponent
import com.juagri.shared.ui.weather.components.WeatherUiState
import com.juagri.shared.utils.toDDMMYYYY
import com.juagri.shared.utils.value
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.util.date.GMTDate
import io.ktor.util.date.GMTDateParser
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun WeatherScreen() {
    var isWeatherNotLoaded = true
    val viewModel = koinViewModel(WeatherViewModel::class)
    val searchWidgetState by viewModel.searchWidgetState
    val searchTextState by viewModel.searchTextState
    val uiState: WeatherUiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.setScreenId(Constants.SCREEN_WEATHER)
    ScreenLayoutWithoutActionBar {
        Scaffold(
            topBar = {
                SearchAppBar(
                    text = searchTextState,
                    onTextChange = { viewModel.updateSearchTextState(it)  },
                    onCloseClicked = { viewModel.updateSearchWidgetState(SearchWidgetState.CLOSED) },
                    onSearchClicked = { viewModel.getWeather(it) }
                )
            },
            content = { paddingValues ->
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreenContent(uiState = uiState, modifier = Modifier, viewModel = viewModel)
                }
            },
        )
        if(isWeatherNotLoaded){
            isWeatherNotLoaded = false
            viewModel.getWeather()
        }
    }
}

@Composable
fun WeatherScreenContent(
    uiState: WeatherUiState,
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel?,
) {
    when {
        uiState.isLoading -> {
            Animation(modifier = Modifier.fillMaxSize())
        }

        uiState.errorMessage.isNotEmpty() -> {
            WeatherErrorState(uiState = uiState, viewModel = viewModel)
        }

        else -> {
            WeatherSuccessState(modifier = modifier, uiState = uiState)
        }
    }
}

@Composable
private fun WeatherErrorState(
    modifier: Modifier = Modifier,
    uiState: WeatherUiState,
    viewModel: WeatherViewModel?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Animation(
            modifier = Modifier
                .fillMaxWidth()
                .weight(8f),
        )

        Button(onClick = { viewModel?.getWeather() }) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Retry",
            )
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                text = "Retry",
                fontWeight = FontWeight.Bold,
            )
        }

        Text(
            modifier = modifier
                .weight(2f)
                .alpha(0.5f)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            text = "Something went wrong: ${uiState.errorMessage}",
            textAlign = TextAlign.Center,
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun WeatherSuccessState(
    modifier: Modifier,
    uiState: WeatherUiState,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = uiState.weather?.name.orEmpty(),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = getWeatherDate(uiState.weather?.date?.value() ?: ""),
            style = MaterialTheme.typography.bodyLarge
        )

        KamelImage(
            modifier = Modifier.size(42.dp),
            resource = asyncPainterResource(data = "https://${uiState.weather?.condition?.icon.orEmpty()}"),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            onLoading = { CircularProgressIndicator(it) },
            onFailure = {
                SnackbarMessage("Error", Color.Red, Color.White)
            },
        )
        Text(
            text = uiState.weather?.temperature.toString(),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp),
            text = uiState.weather?.condition?.text.orEmpty(),
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            modifier = Modifier.padding(bottom = 4.dp),
            text = uiState.weather?.feelsLike.toString(),
            style = MaterialTheme.typography.bodySmall
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(painter = painterResource(DrawableResource("ic_sunrise.png")), contentDescription = null)
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = uiState.weather?.forecasts?.get(0)?.sunrise?.lowercase().orEmpty(),
                style = MaterialTheme.typography.bodySmall,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(painter = painterResource(DrawableResource("ic_sunset.png")), contentDescription = null)
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = uiState.weather?.forecasts?.get(0)?.sunset?.lowercase().orEmpty(),
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
        ) {
            WeatherComponent(
                modifier = Modifier.weight(1f),
                weatherLabel = "wind speed",
                weatherValue = uiState.weather?.wind.toString(),
                weatherUnit = "km/h",
                iconId = "ic_wind.png",
            )
            WeatherComponent(
                modifier = Modifier.weight(1f),
                weatherLabel = "uv",
                weatherValue = uiState.weather?.uv.toString(),
                weatherUnit = "index",
                iconId = "ic_uv.png",
            )
            WeatherComponent(
                modifier = Modifier.weight(1f),
                weatherLabel = "humidity",
                weatherValue = uiState.weather?.humidity.toString(),
                weatherUnit = "percentage %",
                iconId = "ic_humidity.png",
            )
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = "Today",
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier
                .align(Alignment.Start)
                .padding(horizontal = 16.dp),
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 8.dp, start = 16.dp),
        ) {
            uiState.weather?.forecasts?.get(0)?.let { forecast ->
                items(forecast.hour.size) { ind ->
                    val hour = forecast.hour[ind]
                    HourlyComponent(
                        time = hour.time,
                        icon = hour.icon,
                        temperature = hour.temperature
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = "Forecast",
            style = MaterialTheme.typography.bodyMedium,
            modifier = modifier
                .align(Alignment.Start)
                .padding(horizontal = 16.dp),
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 8.dp, start = 16.dp),
        ) {
            uiState.weather?.let { weather ->
                items(weather.forecasts.size) { ind ->
                    val forecast = weather.forecasts[ind]
                    ForecastComponent(
                        date = forecast.date,
                        icon = forecast.icon,
                        minTemp = forecast.minTemp,
                        maxTemp = forecast.maxTemp,
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

private fun getWeatherDate(date: String): String{
    return try {
        stringToGMTDateConverter(date).toDDMMYYYY()
    }catch (e: Exception){
        e.printStackTrace()
        date
    }
}

fun stringToGMTDateConverter(string: String): GMTDate {
    val monthInOrdinal = string.substring(5, 7)
    val adjustedDate = when (monthInOrdinal) {
        "01" -> {
            string.substring(0, 5).plus("JAN").plus(string.substring(7))
        }
        "02" -> {
            string.substring(0, 5).plus("FEB").plus(string.substring(7))
        }
        "03" -> {
            string.substring(0, 5).plus("MAR").plus(string.substring(7))
        }
        "04" -> {
            string.substring(0, 5).plus("APR").plus(string.substring(7))
        }
        "05" -> {
            string.substring(0, 5).plus("MAY").plus(string.substring(7))
        }
        "06" -> {
            string.substring(0, 5).plus("JUN").plus(string.substring(7))
        }
        "07" -> {
            string.substring(0, 5).plus("JUL").plus(string.substring(7))
        }
        "08" -> {
            string.substring(0, 5).plus("AUG").plus(string.substring(7))
        }
        "09" -> {
            string.substring(0, 5).plus("SEP").plus(string.substring(7))
        }
        "10" -> {
            string.substring(0, 5).plus("OCT").plus(string.substring(7))
        }
        "11" -> {
            string.substring(0, 5).plus("NOV").plus(string.substring(7))
        }
        else -> {
            string.substring(0, 5).plus("DEC").plus(string.substring(7))
        }
    }
    println("Weather Date2: $adjustedDate")
    return GMTDateParser("yyyy-MMM-dd").parse(adjustedDate)
}

@Composable
fun WeatherTopAppBar(
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit
) {
    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            DefaultAppBar(
                onSearchClicked = onSearchTriggered
            )
        }

        SearchWidgetState.OPENED -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked,
                onSearchClicked = onSearchClicked
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppBar(onSearchClicked: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "App",
                fontWeight = FontWeight.Bold,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        actions = {
            IconButton(
                onClick = { onSearchClicked() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                )
            }
        }
    )
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        color = MaterialTheme.colorScheme.primary,
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { onTextChange(it) },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = "Search for a city",
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier.alpha(0.7f),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                    keyboardController?.hide()
                },
            ),
        )
    }
}