package com.example.weatherforecast.ui.screens

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitway.ui.theme.AppTheme
import com.example.weatherforecast.R
import com.example.weatherforecast.data.model.WeatherItem
import com.example.weatherforecast.data.model.WeatherNowItemData
import com.example.weatherforecast.ui.viewmodel.MainViewModel
import com.example.weatherforecast.utils.RequestLocationPermission
import com.example.weatherforecast.utils.getUserLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun MainPage(viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val tag = "API Response"
    var latitude: String? by remember { mutableStateOf(null) }
    var longitude: String? by remember { mutableStateOf(null) }
    val location = remember { mutableStateOf("") }




    RequestLocationPermission(
        onPermissionGranted = {
            CoroutineScope(Dispatchers.Main).launch {
                val userLocation = getUserLocation(context)
                latitude = userLocation?.latitude?.toString()
                longitude = userLocation?.longitude?.toString()
                location.value = "$latitude,$longitude"

                if (latitude != null && longitude != null) {
                    viewModel.fetchWeather(location.value, context)
                    Log.i("userLocation", "Latitude: $latitude, Longitude: $longitude")
                }
            }
        },
        onPermissionDenied = {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            Log.e("Location", "Permission denied")
        }
    )



    val response by viewModel.weatherResponse.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)


    val weatherNowList = listOf(
        WeatherNowItemData(
            title = "Feels Like",
            icon = R.drawable.temperature1_ic,
            value = "${response?.current?.feelslike_c} °C"
        ),
        WeatherNowItemData(
            title = "Wind",
            icon = R.drawable.wind1_ic,
            value = "${response?.current?.wind_kph} km/h"
        ),
        WeatherNowItemData(
            title = "Humidity",
            icon = R.drawable.drop1_ic,
            value = "${response?.current?.humidity} %"
        ),
        WeatherNowItemData(
            title = "Precipitation",
            icon = R.drawable.umbrella1_ic,
            value = "${response?.current?.precip_mm} mm"
        ),
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(1f),
        containerColor = AppTheme.colorsScheme.background,
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                //Container 1
                TopBox(modifier = Modifier.weight(0.65f), response)
                //Container 2
                BottomBox(modifier = Modifier.weight(0.4f), weatherNowList, location, viewModel)
            }
        }

    }
}

@Composable
private fun TopBox(modifier: Modifier, response: WeatherItem?) {
    Box(
        modifier = modifier
            .fillMaxHeight(0.7f)
            .fillMaxWidth(1f)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF2795f6), Color(0xff0369ed)),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                ),
                shape = RectangleShape
            )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .fillMaxSize(1f)
        ) {

            //Location name
            Text(
                text = response?.location?.name ?: "Random Location",
                style = AppTheme.typography.titleNormal,
                color = Color.White,
            )
            //Location time
            Text(
                text = response?.location?.localtime ?: "Local time",
                style = AppTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.5f),
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(0.8f),
                painter = painterResource(
                    id = weatherConditionIcon(response?.current?.condition?.text ?: "Sunny"),
                ),
                alpha = 0.7f,
                alignment = Alignment.Center,
                contentDescription = "Weather Icon",
            )
            //Location Temperature
            Text(
                text = "${response?.current?.temp_c?.toInt()} °C",
                style = AppTheme.typography.titleLarge,
                color = Color.White,
            )
            //Weather Condition: Sunny/Rainy/Windy/etc
            Text(
                response?.current?.condition?.text ?: "Sunny",
                style = AppTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.5f),
            )
        }

    }
}

@Composable
private fun BottomBox(
    modifier: Modifier,
    weatherNowList: List<WeatherNowItemData>,
    location: MutableState<String>,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    var textstate by remember { mutableStateOf("") }
    Box(
        modifier = modifier
            .fillMaxWidth(1f)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .fillMaxSize(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                "Weather now",
                textAlign = TextAlign.Start,
                style = AppTheme.typography.titleNormal,
                color = AppTheme.colorsScheme.onBackground,
                modifier = Modifier.fillMaxWidth(1f)
            )
            Spacer(Modifier.padding(vertical = 5.dp))
            LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                items(weatherNowList) { item ->
                    WeatherNowItem(title = item.title, icon = item.icon, value = item.value)
                }

            }

            TextField(
                value = textstate,
                onValueChange = {
                    textstate = it
                    location.value = textstate
                },
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .fillMaxWidth(1f),
                placeholder = {
                    Text(
                        text = "Your location",
                        style = AppTheme.typography.labelSmall,
                    )
                },
                maxLines = 1,
                trailingIcon = {
                    Icon(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(10.dp)
                            .clickable {
                                location.value = textstate
                                viewModel.fetchWeather(location.value, context)
                            },
                        painter = painterResource(id = R.drawable.search1_ic),
                        contentDescription = "Search Icon",
                        tint = AppTheme.colorsScheme.onSecondary
                    )
                },
                keyboardActions = KeyboardActions(
                    onSearch = {
                        // Handle Done action
                        viewModel.fetchWeather(location.value, context)

                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = AppTheme.colorsScheme.onBackground2,
                    unfocusedContainerColor = AppTheme.colorsScheme.onBackground2,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = AppTheme.colorsScheme.onBackground,
                    focusedTextColor = AppTheme.colorsScheme.onBackground,

                    ),
                shape = RoundedCornerShape(24.dp)
            )

        }

    }
}

@Composable
fun WeatherNowItem(title: String, icon: Int, value: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxSize(1f)
    ) {
        Box(
            modifier =
            Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(AppTheme.colorsScheme.onBackground2)
        ) {
            Icon(
                modifier = Modifier.padding(8.dp),
                painter = painterResource(id = icon),
                contentDescription = "Weather Icon"
            )
        }
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = title,
                style = AppTheme.typography.labelSmall,
                color = AppTheme.colorsScheme.onBackground,
            )
            Text(
                text = value,
                style = AppTheme.typography.labelSmall,
                color = AppTheme.colorsScheme.onBackground,
            )
        }
    }
}

fun weatherConditionIcon(condition: String): Int {
    return if (condition.contains("Cloudy", ignoreCase = true)) R.drawable.cloudy1_ic
    else if (condition.contains("Sunny", ignoreCase = true)) R.drawable.sun1_ic
    else if (condition.contains("Rain", ignoreCase = true)) R.drawable.rainy1_ic
    else if (condition.contains("Snow", ignoreCase = true)) R.drawable.snowy1_ic
    else if (condition.contains("Mist", ignoreCase = true)) R.drawable.mist1_ic
    else R.drawable.sun1_ic
}

@Preview(device = Devices.PIXEL_4, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MainPagePreview() {
    AppTheme {
        MainPage()
    }
}