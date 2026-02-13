package com.example.mypersonalweatherapp

sealed class WeatherUiState {
    object Loading : WeatherUiState()

    data class Success(val data: WeatherResponse) : WeatherUiState()

    data class Error(val message: String) : WeatherUiState()
}