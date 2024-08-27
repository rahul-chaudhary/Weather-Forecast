package com.example.weatherforecast

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.fitway.ui.theme.AppTheme
import com.example.weatherforecast.ui.screens.MainPage
import com.example.weatherforecast.utils.RequestLocationPermission
import com.example.weatherforecast.utils.getUserLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                var latitude: String? by remember { mutableStateOf(null) }
                var longitude: String? by remember { mutableStateOf(null) }

                RequestLocationPermission(
                    onPermissionGranted = {
                        CoroutineScope(Dispatchers.IO).launch {
                            val location = getUserLocation(this@MainActivity)
                            latitude = location?.latitude?.toString()
                            longitude = location?.longitude?.toString()
                            Log.i("Location", "Latitude: $latitude, Longitude: $longitude")

                        }
                    },
                    onPermissionDenied = {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                        Log.e("Location", "Permission denied")
                    }
                )
                MainPage(latitude = latitude, longitude = longitude)
            }
        }
    }
}



