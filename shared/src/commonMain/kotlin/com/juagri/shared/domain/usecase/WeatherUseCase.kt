package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.repo.weather.WeatherRepository

class WeatherUseCase(private val repository: WeatherRepository) {

    suspend fun getWeatherDetails(city: String = "Chennai") = repository.getWeatherDetails(city)
}