package com.juagri.shared.data.remote.weather

import com.juagri.shared.data.remote.ApiConfig.WEATHER_URL
import com.juagri.shared.domain.model.weather.ForecastResponse
import com.juagri.shared.domain.repo.weather.WeatherRepository
import com.juagri.shared.utils.JUError
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.value
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class WeatherRepositoryImpl(
    private val client: HttpClient
) : WeatherRepository {

    override suspend fun getWeatherDetails(
        city: String,
        key: String,
        days: Int
    ): Flow<ResponseState<ForecastResponse>> = callbackFlow {
        trySend(ResponseState.Loading(true))
        try {
            val response =
                client.get("$WEATHER_URL?key=$key&days=$days&q=$city").body<ForecastResponse>()
            println(response)
            trySend(ResponseState.Loading())
            trySend(ResponseState.Success(response))
        }catch (e: Exception){
            trySend(ResponseState.Loading())
            trySend(ResponseState.Error(JUError.CustomError(e.message.value())))
        }
        awaitClose {
            channel.close()
        }
    }
}