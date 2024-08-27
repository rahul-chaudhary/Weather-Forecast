package com.example.weatherforecast.data.source

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.example.weatherforecast.data.MySingleton
import com.example.weatherforecast.data.model.WeatherItem
import com.example.weatherforecast.utils.consts
import com.example.weatherforecast.utils.consts.API_KEY
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import java.nio.charset.Charset
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


suspend fun apiResponse(location: String, context: Context): WeatherItem? {
    val tag = "API Response"
    val token = consts.API_KEY
    val BASE_URL = "https://api.weatherapi.com/v1/current.json?key=$API_KEY&q=$location&aqi=no"
    return suspendCancellableCoroutine { continuation ->

        val myJsonRequest = object : JsonObjectRequest(
            Method.GET,
            BASE_URL,
            null,
            Response.Listener { response ->
                Log.d(tag, response.toString())
                try {
                    val apiResponse = Gson().fromJson(response.toString(), WeatherItem::class.java)
                    continuation.resume(apiResponse)
                } catch (e: JsonSyntaxException) {
                    Log.e("$tag Error", "JSON Parsing Error", e)
                    continuation.resumeWithException(e)
                }
            },
            Response.ErrorListener { error ->
                Log.e("$tag Error", "Network Error: ${error.message}", error)
                continuation.resumeWithException(Exception(error.message ?: "Unknown error"))
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"
                return headers
            }

            override fun getBody(): ByteArray {
                return JSONObject().toString().toByteArray(Charset.defaultCharset())
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
                return if (response.data.isEmpty()) {
                    val responseData = "{}".toByteArray(Charset.defaultCharset())
                    val modifiedResponse = NetworkResponse(
                        response.statusCode,
                        responseData,
                        response.headers,
                        response.notModified
                    )
                    super.parseNetworkResponse(modifiedResponse)
                } else {
                    val jsonString = response.data.toString(Charsets.UTF_8)
                    val jsonObject = JSONObject(jsonString)
                    Response.success(
                        jsonObject,
                        HttpHeaderParser.parseCacheHeaders(response)
                    )
                }
            }
        }

        // Set a custom retry policy with timeout and retry attempts
        val retryPolicy = DefaultRetryPolicy(
            10000, // Increase timeout if needed
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        myJsonRequest.retryPolicy = retryPolicy
        MySingleton.getInstance(context).addToRequestQueue(myJsonRequest)
    }
}