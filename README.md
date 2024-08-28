# Weather Forecast App
A simple weather forecasting app built using Jetpack Compose and Kotlin. This app fetches weather data based on the user's current location or a specified location.


https://github.com/user-attachments/assets/5f439aa8-4298-4192-9949-ddb32bb02fee


## Table of Contents
- Features
- Architecture
- Setup Instructions
- Assumptions and Limitations
- License

## Features
- Real-time weather updates for the user's current location.
- Search for weather information by city.
- Displays key weather metrics like temperature, wind speed, humidity, etc.
- Responsive UI built with Jetpack Compose.
  
## Architecture
The Weather Forecast App follows the MVVM (Model-View-ViewModel) architecture pattern, which helps to separate concerns and improves testability. Below is a brief overview of the architecture:

- Model: Represents the data layer of the app. In this app, data is fetched from a remote API and is encapsulated in data classes like WeatherItem.

- View: This is the UI layer, built using Jetpack Compose. It observes data from the ViewModel and updates the UI accordingly.

- ViewModel: Acts as a bridge between the Model and View. The MainViewModel handles the business logic and exposes data to the UI through LiveData. It also manages the fetching of weather data and handles location permissions.

- Coroutines: Coroutines are used for managing asynchronous tasks such as fetching data from the network or accessing the user's location without blocking the main thread.

- Repository: The app follows a repository pattern where the WeatherRepository abstracts the data source (network, database) from the rest of the app, providing a clean API for accessing weather data.

## Setup Instructions
To get started with the Weather Forecast App:

### 1. Clone the Repository:

```
git clone https://github.com/yourusername/weather-forecast-app.git
cd weather-forecast-app
```
### 2.  Open in Android Studio:

- Open Android Studio.
- Click on "Open an existing Android Studio project".
- Select the cloned repository folder.

### 3. API Key Setup:

- The app uses a weather API to fetch weather data. You'll need to obtain an API key and add it to the app:
Register for an API key from WeatherAPI.
 - Create a consts.kt file under utils and add your API key:
```
package com.example.weatherforecast.utils

object consts {
    const val API_KEY = "YOUR_API_KEY_HERE"
    const val BASE_URL = "https://api.weatherapi.com/v1/current.json"
}
```
### 4. Run the App:

- Connect your Android device or start an emulator.
- Click on the "Run" button in Android Studio to build and run the app.

## Assumptions and Limitations
### Assumptions
- The app assumes that the user will grant location permissions when requested.
- The app is designed to work with the WeatherAPI, and it assumes that the API responses are consistent with the current implementation.
## Limitations
- API Rate Limits: The free tier of WeatherAPI has rate limits, which could restrict the frequency of weather updates.
- Location Accuracy: The app uses the last known location from FusedLocationProviderClient, which may not always be accurate.
- Network Dependency: The app requires an active internet connection to fetch weather data. No offline caching mechanism is implemented.
- Limited Error Handling: Currently, error handling is basic, with no detailed error messages or retry mechanisms.
