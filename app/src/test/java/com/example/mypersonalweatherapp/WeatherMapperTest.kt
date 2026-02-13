package com.example.mypersonalweatherapp

import com.example.mypersonalweatherapp.WeatherMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherMapperTest {

    @Test
    fun `when time is 7 PM, should return moon icon regardless of weather`() {
        // GIVEN: It is 7 PM (19:00)
        val testHour = 19
        val weatherType = "Sunny"

        // WHEN: We call our icon logic
        val resultIcon = WeatherMapper.getWeatherIconForTest(weatherType, testHour)

        // THEN: It should return the moon icon
        assertEquals(R.drawable.ic_moon, resultIcon)
    }

    @Test
    fun `when time is 10 AM and it is raining, should return rain icon`() {
        // GIVEN: It is 10 AM and Raining
        val testHour = 10
        val weatherType = "Rain"

        // WHEN:
        val resultIcon = WeatherMapper.getWeatherIconForTest(weatherType, testHour)

        // THEN:
        assertEquals(R.drawable.ic_rain, resultIcon)
    }

    @Test
    fun `when time is 10 AM and it is sunny, should return sun icon`() {
        // GIVEN: It is 10 AM and Clear
        val testHour = 10
        val weatherType = "Clear"

        // WHEN:
        val resultIcon = WeatherMapper.getWeatherIconForTest(weatherType, testHour)

        // THEN:
        assertEquals(R.drawable.ic_sun, resultIcon)
    }
}