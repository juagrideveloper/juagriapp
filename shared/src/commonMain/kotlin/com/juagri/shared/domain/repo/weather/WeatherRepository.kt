package com.juagri.shared.domain.repo.weather

import com.juagri.shared.domain.model.weather.ForecastResponse
import com.juagri.shared.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherDetails(
        city: String = "",
        key: String = "a3267fef02dd4c56983110356241408",
        days: Int = 10
    ): Flow<ResponseState<ForecastResponse>>
}