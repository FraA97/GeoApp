package com.example.mapsproject.SinglePlayer.fragment

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mapsproject.Configuration.SinglePlayerServerConf.Companion.FirstReq
import com.example.mapsproject.Configuration.SinglePlayerServerConf.Companion.url
import com.example.mapsproject.R
import org.json.JSONObject

class LatLongFragment: Fragment() {
    var queue : RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_loading, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createLevel()
        //  Handler(Looper.getMainLooper()).postDelayed({check()},Conf.pollingPeriod)
    }

    private fun createLevel() {
        Log.i("myTag","request: "+url+"req="+ FirstReq+"&level=0")
        val stringRequest = StringRequest(
            Request.Method.GET, url+"req="+ FirstReq+"&level=0",{
                response->
                val reply = JSONObject(response.toString())
                val lat = reply!!.getDouble("lat")
                val long = reply!!.getDouble("long")
                val responseCountry = reply!!.getString("responseCountry")
                val responseCity = reply!!.getString("responseCity")

                //save everything on resource
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
                val editor = sharedPref?.edit()
                editor?.putFloat("lat", lat.toFloat())
                editor?.putFloat("long", long.toFloat())
                editor?.putString("city", responseCity)
                editor?.putString("country", responseCountry)
                editor?.apply()

                Log.i("myTag","lat: "+lat+", long: "+long+", City: "+responseCity+", Country: "+responseCountry)

                findNavController().navigate(R.id.action_latLongFragment_to_mapFragment2)

            },{ error: VolleyError? ->
                Log.i("info", "Polling: " + error.toString())
            })
        queue?.add(stringRequest)
    }

}