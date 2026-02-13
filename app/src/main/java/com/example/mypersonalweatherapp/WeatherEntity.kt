package com.example.mypersonalweatherapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_history")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cityName: String,
    val country: String,
    val temperature: Double,
    val weatherDescription: String,
    val timestamp: Long = System.currentTimeMillis()
)
