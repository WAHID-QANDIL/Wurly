package com.wahid.wurly.presentation.screen.forecast

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wahid.wurly.R
import com.wahid.wurly.domain.usecase.GetCityForecastById
import com.wahid.wurly.domain.usecase.GetNextForecastDays
import com.wahid.wurly.presentation.common.model.ForecastDayItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val getNextForecastDays: GetNextForecastDays,
    private val getCityForecastById: GetCityForecastById,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    companion object {
        private const val TAG = "ForecastViewModel"
    }

    private val _uiState = MutableStateFlow<ForecastUiState>(ForecastUiState.Loading)
    val uiState: StateFlow<ForecastUiState> = _uiState.asStateFlow()

    private val cityId: Long? = savedStateHandle.get<Long>("cityId")

    init {
        if (cityId != null) {
            loadForecastForCity(cityId)
        } else {
            loadForecast()
        }
    }

    private fun loadForecast() {
        viewModelScope.launch {
            getNextForecastDays().collect { days ->
                val forecastItems = days?.list?.map { day ->
                    Log.d(TAG, "loadForecast: $day")
                    ForecastDayItem(
                        condition = day.condition.description,
                        dayName = day.dayTime.toDayName(zoneId = ZoneId.systemDefault()),
                        conditionIcon = day.condition.icon,
                        highTemp = day.tempMax.toString(),
                        lowTemp = day.tempMin.toString(),
                        date = formatDate(day.dayTime),
                        time = formatTime(day.dayTime),
                    )
                } ?: emptyList()


                _uiState.value = ForecastUiState.Success(
                    forecasts = forecastItems,
                    currentBackground = R.drawable.image,
                )
            }
        }
    }

    private fun loadForecastForCity(cityId: Long) {
        viewModelScope.launch {
            runCatching { getCityForecastById(cityId) }
                .onSuccess { days ->
                    val forecastItems = days.list.map { day ->
                        ForecastDayItem(
                            condition = day.condition.description,
                            dayName = day.dayTime.toDayName(zoneId = ZoneId.systemDefault()),
                            conditionIcon = day.condition.icon,
                            highTemp = day.tempMax.toString(),
                            lowTemp = day.tempMin.toString(),
                            date = formatDate(day.dayTime),
                            time = formatTime(day.dayTime),
                        )
                    }
                    _uiState.value = ForecastUiState.Success(
                        forecasts = forecastItems,
                        currentBackground = R.drawable.image,
                    )
                }
                .onFailure { throwable ->
                    _uiState.value = ForecastUiState.Error(throwable.message ?: "Error")
                }
        }
    }

    private fun Long.toDayName(zoneId: ZoneId = ZoneId.of("UTC")): String {
        val localDate = Instant.ofEpochSecond(this).atZone(zoneId).toLocalDate()
        val formatter = DateTimeFormatter.ofPattern("EEEE", Locale.ENGLISH)
        return localDate.format(formatter)
    }

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    private fun formatDate(epochSeconds: Long): String =
        Instant.ofEpochSecond(epochSeconds).atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter)

    private fun formatTime(epochSeconds: Long): String =
        Instant.ofEpochSecond(epochSeconds).atZone(ZoneId.systemDefault()).toLocalTime().format(timeFormatter)
}