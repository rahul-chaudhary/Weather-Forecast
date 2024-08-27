package com.example.weatherforecast.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.model.WeatherItem
import com.example.weatherforecast.data.source.apiResponse
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _weatherResponse = MutableLiveData<WeatherItem?>()
    val weatherResponse: LiveData<WeatherItem?> = _weatherResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _location = MutableLiveData<String>()
    val location: LiveData<String> = _location

    fun fetchWeather(location: String, context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiResponse(location = location, context = context)
                _weatherResponse.value = response
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching weather data", e)
                _weatherResponse.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setLocation(location: String) {
        _location.value = location
    }
}