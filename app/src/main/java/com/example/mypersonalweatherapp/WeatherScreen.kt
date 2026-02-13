package com.example.mypersonalweatherapp

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun WeatherAppMainScreen(viewModel: WeatherViewModel = hiltViewModel()) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Current Weather", "History")

    Column {
        // 2. The Header (Tabs)
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = { Text(title, modifier = Modifier.padding(16.dp)) }
                )
            }
        }

        when (tabIndex) {
            0 -> CurrentWeatherTab(viewModel) // Shows Current Temp, Icon, Sun/Moon
            1 -> WeatherHistoryTab(viewModel) // Shows the list from Room DB
        }
    }
}

