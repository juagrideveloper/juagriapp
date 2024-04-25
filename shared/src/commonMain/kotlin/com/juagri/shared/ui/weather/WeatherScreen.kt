package com.juagri.shared.ui.weather

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.juagri.shared.ui.components.fields.TextTitle
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar

@Composable
fun WeatherScreen() {
    ScreenLayoutWithoutActionBar(modifier= Modifier.background(Color.Blue)) {
        TextTitle("Weather")
    }
}

@Composable
fun WeatherScreen1() {
    ScreenLayoutWithoutActionBar(modifier= Modifier.background(Color.Blue)) {
        TextTitle("Weather")
    }
}

@Composable
fun WeatherScreen2() {
    ScreenLayoutWithoutActionBar(modifier= Modifier.background(Color.Blue)) {
        TextTitle("Weather")
    }
}