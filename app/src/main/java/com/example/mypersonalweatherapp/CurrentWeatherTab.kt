package com.example.mypersonalweatherapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Instant
import java.time.format.DateTimeFormatter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import java.time.ZoneOffset

@Composable
fun CurrentWeatherTab(viewModel: WeatherViewModel) {
    val state by viewModel.uiState.collectAsState()
    var cityInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Determine colors based on Day/Night for better Glassmorphism contrast
    val isDayTime = if (state is WeatherUiState.Success) {
        val weather = (state as WeatherUiState.Success).data
        weather.dt in weather.sys.sunrise until weather.sys.sunset
    } else true

    // Dynamic Background Gradient
    val backgroundBrush = if (isDayTime) {
        // Vibrant Day Purple
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF6A1B9A), // Rich Purple
                Color(0xFF4527A0), // Deep Indigo
                Color(0xFF283593)  // Midnight Blue
            )
        )
    } else {
        // Moody Night Purple/Black
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF311B92), // Dark Violet
                Color(0xFF1A237E), // Navy
                Color(0xFF0D0D0D)  // Near Black
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(backgroundBrush)) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- SEARCH BAR ---
            OutlinedTextField(
                value = cityInput,
                onValueChange = { cityInput = it },
                label = { Text("Enter City Name", color = Color.White.copy(alpha = 0.7f)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        if (cityInput.isNotBlank()) {
                            viewModel.fetchWeather(cityInput)
                            focusManager.clearFocus()
                        }
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    if (cityInput.isNotBlank()) {
                        viewModel.fetchWeather(cityInput)
                        focusManager.clearFocus()
                    }
                }),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- 2. WEATHER CONTENT ---
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                when (val uiState = state) {
                    is WeatherUiState.Loading -> CircularProgressIndicator(color = Color.White)
                    is WeatherUiState.Success -> {
                        val weather = uiState.data
                        val weatherType = weather.weather.firstOrNull()?.main ?: ""

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // MAIN GLASS CARD (Sharp text, transparent background)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(32.dp))
                                    .background(Color.White.copy(alpha = 0.12f)) // Subtle glass
                                    .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(32.dp))
                                    .padding(vertical = 32.dp, horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${weather.name}, ${weather.sys.country}",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )

                                    Icon(
                                        painter = painterResource(id = WeatherMapper.getWeatherIcon(weatherType, isDayTime)),
                                        contentDescription = weatherType,
                                        modifier = Modifier.size(160.dp),
                                        tint = Color.Unspecified
                                    )

                                    Text(
                                        text = "${weather.main.temp.toInt()}°C",
                                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 80.sp),
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )

                                    Text(
                                        text = weatherType,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // SUNRISE/SUNSET CARDS
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                GlassyWeatherCard(Modifier.weight(1f)) {
                                    WeatherDetailItem("Sunrise", weather.sys.sunrise, weather.timezone)
                                }
                                GlassyWeatherCard(Modifier.weight(1f)) {
                                    WeatherDetailItem("Sunset", weather.sys.sunset, weather.timezone)
                                }
                            }
                        }
                    }
                    else -> {
                        Text("Search for a city!", color = Color.White.copy(alpha = 0.6f))
                    }
                }
            }
        }
    }
}

@Composable
fun GlassyWeatherCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .height(110.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.15f))
            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun WeatherDetailItem(label: String, time: Long, timezoneOffset: Int) {
    val formattedTime = remember(time, timezoneOffset) {
        val zoneOffset = ZoneOffset.ofTotalSeconds(timezoneOffset)
        Instant.ofEpochSecond(time)
            .atOffset(zoneOffset)
            .format(DateTimeFormatter.ofPattern("hh:mm a"))
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelLarge)
        Text(text = formattedTime, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
    }
}

