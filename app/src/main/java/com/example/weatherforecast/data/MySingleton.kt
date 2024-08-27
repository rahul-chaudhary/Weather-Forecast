package com.example.weatherforecast.data

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class MySingleton constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: MySingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MySingleton(context).also {
                    INSTANCE = it
                }
            }
    }

    fun fetchAndParseJson(url: String, onSuccess: (JSONObject) -> Unit, onError: (VolleyError) -> Unit) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                onSuccess(response)
            },
            { error ->
                onError(error)
            }
        )

        // Add the request to the request queue
        requestQueue.add(jsonObjectRequest)
    }



    //making this function private so nobody should have access to it
    val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }
    //Function to add the request to the queue
    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}


