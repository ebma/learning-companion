package de.htwberlin.learningcompanion.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.osmdroid.util.GeoPoint

class LocationToAddressRequest(val context: Context) {

    interface Callback {
        fun onResult(address: String)
        fun onError(errorMessage: String)
    }

    private lateinit var callback: Callback

    fun getAddressForLocation(location: GeoPoint, callback: Callback) {
        this.callback = callback

        VolleyService.initialize(context.applicationContext)

        val queue = VolleyService.requestQueue
        val url = createURL(location)

        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response -> handleResponse(response) },
                Response.ErrorListener { error -> handleError(error) }
        )
//        {
//            override fun getParams(): MutableMap<String, String> {
//                val headers = HashMap<String, String>()
//                headers["Content-Type"] = "application/json"
//                return headers
//            }
//        } as JsonObjectRequest

        queue.add(stringRequest)
//        queue.start()
    }

    private fun createURL(location: GeoPoint): String {
        return "https://nominatim.openstreetmap.org/reverse?format=json&lat=${location.latitude}&lon=${location.longitude}"
    }

    private fun handleResponse(response: String) {
        val address = tryGetAddressFromJSONString(response) ?: ""
        callback.onResult(address)
    }

    private fun tryGetAddressFromJSONString(jsonString: String): String? {
        return try {

            ""
        } catch (e: JSONException) {
            e.printStackTrace()
            null
        }
    }

    private fun handleError(error: VolleyError) {
        error.printStackTrace()
        callback.onError(error.message
                ?: "An error occurred with your request. Code: ${error.networkResponse.statusCode}")
    }
}