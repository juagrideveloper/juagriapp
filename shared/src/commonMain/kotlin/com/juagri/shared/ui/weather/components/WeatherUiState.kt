package com.juagri.shared.ui.weather.components

import com.juagri.shared.domain.model.weather.Weather

data class WeatherUiState(
    val weather: Weather? = null,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
)