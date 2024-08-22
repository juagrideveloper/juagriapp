package com.juagri.shared.ui.weather

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.weather.toWeather
import com.juagri.shared.domain.usecase.WeatherUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.ui.weather.components.SearchWidgetState
import com.juagri.shared.ui.weather.components.WeatherUiState
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class WeatherViewModel(
    dataManager: DataManager,
    session: SessionPreference,
    private val weatherUseCase: WeatherUseCase,
): BaseViewModel(session, dataManager) {

    private val _uiState: MutableStateFlow<WeatherUiState> =
        MutableStateFlow(WeatherUiState(isLoading = true))
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _searchWidgetState: MutableState<SearchWidgetState> =
        mutableStateOf(value = SearchWidgetState.CLOSED)
    val searchWidgetState: State<SearchWidgetState> = _searchWidgetState

    private val _searchTextState: MutableState<String> = mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    fun updateSearchWidgetState(newValue: SearchWidgetState) {
        _searchWidgetState.value = newValue
    }

    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }

    fun getWeather(city: String = "Chennai") {
        backgroundScope {
            weatherUseCase.getWeatherDetails(city).collect{response->
                withContext(Dispatchers.Main) {
                    when (response) {
                        is ResponseState.Loading -> _uiState.value =  WeatherUiState(isLoading = response.isLoading)
                        is ResponseState.Success -> _uiState.value = WeatherUiState( weather = response.data.toWeather())
                        is ResponseState.Error -> _uiState.value = WeatherUiState(errorMessage = response.e?.message.value())
                    }
                }
            }
        }
    }
}