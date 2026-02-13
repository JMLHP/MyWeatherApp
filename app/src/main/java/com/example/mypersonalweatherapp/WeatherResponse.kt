package com.example.mypersonalweatherapp

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val name: String,             // City Name
    val sys: Sys,                 // Country, Sunrise, Sunset
    val main: Main,               // Temperature
    val weather: List<Weather>,   // Weather description (Rain, Clear, etc.)
    val dt: Long,                  // Calculation time (Unix)
    val timezone: Int
)
data class Sys(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)
data class Main(
    val temp: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double
)

data class Weather(
    val main: String,
    val description: String
)
