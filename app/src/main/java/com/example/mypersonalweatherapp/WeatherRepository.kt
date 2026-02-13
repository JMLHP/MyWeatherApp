package com.example.mypersonalweatherapp

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val weatherDao: WeatherDao
) {
    suspend fun getAndSaveWeather(city: String, apiKey: String): WeatherResponse {
        val response = weatherApi.getCurrentWeather(city, apiKey, "metric")

        val entity = WeatherEntity(
            cityName = response.name,
            country = response.sys.country,
            temperature = response.main.temp,
            weatherDescription = response.weather.firstOrNull()?.main ?: "Unknown",
            timestamp = System.currentTimeMillis()
        )

        weatherDao.insertWeather(entity)

        return response
    }

    fun getWeatherHistory(): Flow<List<WeatherEntity>> {
        return weatherDao.getAllHistory()
    }
}