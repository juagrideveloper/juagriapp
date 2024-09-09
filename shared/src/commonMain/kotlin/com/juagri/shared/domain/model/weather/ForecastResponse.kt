package com.juagri.shared.domain.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    @SerialName("current") val current: Current,
    @SerialName("forecast") val forecast: NetworkForecast,
    @SerialName("location") val location: Location
) {
    @Serializable
    data class Current(
        @SerialName("cloud") val cloud: Int,
        @SerialName("condition") val condition: Condition,
        @SerialName("feelslike_c") val feelslikeC: Double,
        @SerialName("feelslike_f") val feelslikeF: Double,
        @SerialName("gust_kph") val gustKph: Double,
        @SerialName("gust_mph") val gustMph: Double,
        @SerialName("humidity") val humidity: Int,
        @SerialName("is_day") val isDay: Int,
        @SerialName("last_updated") val lastUpdated: String,
        @SerialName("last_updated_epoch") val lastUpdatedEpoch: Int,
        @SerialName("precip_in") val precipIn: Double,
        @SerialName("precip_mm") val precipMm: Double,
        @SerialName("pressure_in") val pressureIn: Double,
        @SerialName("pressure_mb") val pressureMb: Double,
        @SerialName("temp_c") val tempC: Double,
        @SerialName("temp_f") val tempF: Double,
        @SerialName("uv") val uv: Double,
        @SerialName("vis_km") val visKm: Double,
        @SerialName("vis_miles") val visMiles: Double,
        @SerialName("wind_degree") val windDegree: Int,
        @SerialName("wind_dir") val windDir: String,
        @SerialName("wind_kph") val windKph: Double,
        @SerialName("wind_mph") val windMph: Double
    ) {
        @Serializable
        data class Condition(
            @SerialName("code") val code: Int,
            @SerialName("icon") val icon: String,
            @SerialName("text") val text: String
        )
    }

    @Serializable
    data class NetworkForecast(
        @SerialName("forecastday") val forecastday: List<NetworkForecastday>
    ) {
        @Serializable
        data class NetworkForecastday(
            @SerialName("astro") val astro: Astro,
            @SerialName("date") val date: String,
            @SerialName("date_epoch") val dateEpoch: Int,
            @SerialName("day") val day: Day,
            @SerialName("hour") val hour: List<NetworkHour>
        ) {
            @Serializable
            data class Astro(
                @SerialName("is_moon_up") val isMoonUp: Int,
                @SerialName("is_sun_up") val isSunUp: Int,
                @SerialName("moon_illumination") val moonIllumination: String,
                @SerialName("moon_phase") val moonPhase: String,
                @SerialName("moonrise") val moonrise: String,
                @SerialName("moonset") val moonset: String,
                @SerialName("sunrise") val sunrise: String,
                @SerialName("sunset") val sunset: String
            )

            @Serializable
            data class Day(
                @SerialName("avghumidity") val avghumidity: Double,
                @SerialName("avgtemp_c") val avgtempC: Double,
                @SerialName("avgtemp_f") val avgtempF: Double,
                @SerialName("avgvis_km") val avgvisKm: Double,
                @SerialName("avgvis_miles") val avgvisMiles: Double,
                @SerialName("condition") val condition: Condition,
                @SerialName("daily_chance_of_rain") val dailyChanceOfRain: Int,
                @SerialName("daily_chance_of_snow") val dailyChanceOfSnow: Int,
                @SerialName("daily_will_it_rain") val dailyWillItRain: Int,
                @SerialName("daily_will_it_snow") val dailyWillItSnow: Int,
                @SerialName("maxtemp_c") val maxtempC: Double,
                @SerialName("maxtemp_f") val maxtempF: Double,
                @SerialName("maxwind_kph") val maxwindKph: Double,
                @SerialName("maxwind_mph") val maxwindMph: Double,
                @SerialName("mintemp_c") val mintempC: Double,
                @SerialName("mintemp_f") val mintempF: Double,
                @SerialName("totalprecip_in") val totalprecipIn: Double,
                @SerialName("totalprecip_mm") val totalprecipMm: Double,
                @SerialName("totalsnow_cm") val totalsnowCm: Double,
                @SerialName("uv") val uv: Double
            ) {
                @Serializable
                data class Condition(
                    @SerialName("code") val code: Int,
                    @SerialName("icon") val icon: String,
                    @SerialName("text") val text: String
                )
            }

            @Serializable
            data class NetworkHour(
                @SerialName("chance_of_rain") val chanceOfRain: Int,
                @SerialName("chance_of_snow") val chanceOfSnow: Int,
                @SerialName("cloud") val cloud: Int,
                @SerialName("condition") val condition: Condition,
                @SerialName("dewpoint_c") val dewpointC: Double,
                @SerialName("dewpoint_f") val dewpointF: Double,
                @SerialName("feelslike_c") val feelslikeC: Double,
                @SerialName("feelslike_f") val feelslikeF: Double,
                @SerialName("gust_kph") val gustKph: Double,
                @SerialName("gust_mph") val gustMph: Double,
                @SerialName("heatindex_c") val heatindexC: Double,
                @SerialName("heatindex_f") val heatindexF: Double,
                @SerialName("humidity") val humidity: Int,
                @SerialName("is_day") val isDay: Int,
                @SerialName("precip_in") val precipIn: Double,
                @SerialName("precip_mm") val precipMm: Double,
                @SerialName("pressure_in") val pressureIn: Double,
                @SerialName("pressure_mb") val pressureMb: Double,
                @SerialName("temp_c") val tempC: Double,
                @SerialName("temp_f") val tempF: Double,
                @SerialName("time") val time: String,
                @SerialName("time_epoch") val timeEpoch: Int,
                @SerialName("uv") val uv: Double,
                @SerialName("vis_km") val visKm: Double,
                @SerialName("vis_miles") val visMiles: Double,
                @SerialName("will_it_rain") val willItRain: Int,
                @SerialName("will_it_snow") val willItSnow: Int,
                @SerialName("wind_degree") val windDegree: Int,
                @SerialName("wind_dir") val windDir: String,
                @SerialName("wind_kph") val windKph: Double,
                @SerialName("wind_mph") val windMph: Double,
                @SerialName("windchill_c") val windchillC: Double,
                @SerialName("windchill_f") val windchillF: Double
            ) {
                @Serializable
                data class Condition(
                    @SerialName("code") val code: Int,
                    @SerialName("icon") val icon: String,
                    @SerialName("text") val text: String
                )
            }
        }
    }

    @Serializable
    data class Location(
        @SerialName("country") val country: String,
        @SerialName("lat") val lat: Double,
        @SerialName("localtime") val localtime: String,
        @SerialName("localtime_epoch") val localtimeEpoch: Int,
        @SerialName("lon") val lon: Double,
        @SerialName("name") val name: String,
        @SerialName("region") val region: String,
        @SerialName("tz_id") val tzId: String
    )
}

data class Weather(
    val temperature: Int,
    val date: String,
    val wind: Int,
    val humidity: Int,
    val feelsLike: Int,
    val condition: ForecastResponse.Current.Condition,
    val uv: Int,
    val name: String,
    val forecasts: List<Forecast>
)


data class Forecast(
    val date: String,
    val maxTemp: String,
    val minTemp: String,
    val sunrise: String,
    val sunset: String,
    val icon: String,
    val hour: List<Hour>,
)

data class Hour(
    val time: String,
    val icon: String,
    val temperature: String,
)

fun ForecastResponse.toWeather(): Weather = Weather(
    temperature = current.tempC.toInt(),
    date = forecast.forecastday[0].date,
    wind = current.windKph.toInt(),
    humidity = current.humidity,
    feelsLike = current.feelslikeC.toInt(),
    condition = current.condition,
    uv = current.uv.toInt(),
    name = location.name,
    forecasts = forecast.forecastday.map { networkForecastday ->
        networkForecastday.toWeatherForecast()
    }
)

fun ForecastResponse.NetworkForecast.NetworkForecastday.toWeatherForecast(): Forecast = Forecast(
    date = date,
    maxTemp = day.maxtempC.toInt().toString(),
    minTemp = day.mintempC.toInt().toString(),
    sunrise = astro.sunrise,
    sunset = astro.sunset,
    icon = day.condition.icon,
    hour = hour.map { networkHour ->
        networkHour.toHour()
    }
)

fun ForecastResponse.NetworkForecast.NetworkForecastday.NetworkHour.toHour(): Hour = Hour(
    time = time,
    icon = condition.icon,
    temperature = tempC.toInt().toString(),
)