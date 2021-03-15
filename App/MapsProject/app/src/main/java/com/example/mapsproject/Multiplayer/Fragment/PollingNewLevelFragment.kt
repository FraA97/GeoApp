package com.example.mapsproject.Multiplayer.Fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Configuration.MultiPlayerServerConf.Companion.queue
import com.example.mapsproject.R
import org.json.JSONObject

class PollingNewLevelFragment: Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(context)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_loading, container, false)
        rootView.findViewById<TextView>(R.id.loading_tv).setText("Waiting server response")
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //poolNewGame()
        Handler(Looper.getMainLooper()).postDelayed({poolNewLevel()}, MultiPlayerServerConf.pollingPeriod)
    }

    private fun poolNewLevel() {
        Log.i("myTag","request: "+ MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.startLevelReq+
                "&player_id="+ MultiPlayerServerConf.player_id+"&game_id="+ MultiPlayerServerConf.game_id+"&level="+MultiPlayerServerConf.played_levels)
        val stringRequest = StringRequest(
                Request.Method.GET,   MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.startLevelReq+
                "&player_id="+ MultiPlayerServerConf.player_id+"&game_id="+ MultiPlayerServerConf.game_id+"&level="+MultiPlayerServerConf.played_levels,{
            response->
            val reply = JSONObject(response.toString())
            val waiting = reply!!.getBoolean("error")
            val msg = reply!!.getString("msg")
            Log.i("myTag","waiting: "+waiting+", msg: "+msg)

            if (!waiting){

                val country = reply!!.getString("Country")
                val city = reply!!.getString("City")
                val fCountry1 = reply!!.getString("fCountry1")
                val fCity1 = reply!!.getString("fCity1")
                val fCountry2 = reply!!.getString("fCountry2")
                val fCity2 = reply!!.getString("fCity2")
                val fCountry3 = reply!!.getString("fCountry3")
                val fCity3 = reply!!.getString("fCity3")

                val lat = reply!!.getDouble("lat")
                val long = reply!!.getDouble("long")

                Log.i("myTag","country: "+country+", city: "+city+
                                        ", fCountry1: "+fCountry1+", fCity1: "+fCity1+
                                        ", fCountry2: "+fCountry2+", fCity2: "+fCity2+
                                        ", fCountry3: "+fCountry3+", fCity3: "+fCity3)

                //save everything on resource
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
                val editor = sharedPref?.edit()
                editor?.putString("country", country.toString())
                editor?.putString("city", city.toString())
                editor?.putString("fCountry1", fCountry1.toString())
                editor?.putString("fCity1", fCity1.toString())
                editor?.putString("fCountry2", fCountry2.toString())
                editor?.putString("fCity2", fCity2.toString())
                editor?.putString("fCountry3", fCountry3.toString())
                editor?.putString("fCity3", fCity3.toString())
                editor?.putFloat("lat", lat.toFloat())
                editor?.putFloat("long", long.toFloat())
                editor?.apply()

                MultiPlayerServerConf.queue?.cancelAll(activity)
                findNavController().navigate(R.id.action_pollingNewLevelFragment_to_mapFragmentMP)


            }
            else
                Handler(Looper.getMainLooper()).postDelayed({poolNewLevel()}, MultiPlayerServerConf.pollingPeriod)

        },{ error: VolleyError? ->
            Log.i("info", "Polling: " + error.toString())
            Toast.makeText(activity,"Error:" + error.toString(), Toast.LENGTH_SHORT)
            findNavController().navigate(R.id.action_pollingNewLevelFragment_to_startFragmentMP)
        })
        stringRequest.setRetryPolicy(DefaultRetryPolicy(
                20 * 1000,  //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        queue?.add(stringRequest)

    }
}