package com.example.mypersonalweatherapp

import com.example.mypersonalweatherapp.WeatherMapper.getWeatherIcon

object WeatherMapper {
    fun getWeatherIcon(weatherType: String, isDay: Boolean): Int {
        return when (weatherType) {
            "Clear" -> if (isDay) R.drawable.ic_sun else R.drawable.ic_moon

            // For clouds, if it's night to show the moon variant
            "Clouds" -> if (isDay) R.drawable.ic_clouds else R.drawable.ic_clouds_night

            "Rain", "Drizzle", "Thunderstorm" -> R.drawable.ic_rain

            // Default case
            else -> if (isDay) R.drawable.ic_sun else R.drawable.ic_moon
        }
    }
    fun getWeatherIconForTest(weatherType: String, isDay: Boolean): Int {
    return getWeatherIcon(weatherType, isDay)
    }
}
