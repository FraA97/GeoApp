package com.example.mapsproject.SinglePlayer.fragment

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.Configuration.SinglePlayerServerConf.Companion.SecondReq
import com.example.mapsproject.R
import org.json.JSONObject
import java.util.concurrent.TimeUnit


class OptionsFragment:Fragment() {
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
        getOptions()
        //  Handler(Looper.getMainLooper()).postDelayed({check()},Conf.pollingPeriod)
    }

    private fun getOptions() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
        val city = sharedPref?.getString("city", "").toString()
        val country = sharedPref?.getString("country", "").toString()

        Log.i("myTag", "request: " + SinglePlayerServerConf.url + "req=" + SecondReq + "&level=0"+"&country=\"" + country + "\"&city=\"" + city + "\"")
        val stringRequest = StringRequest(
                Request.Method.GET, SinglePlayerServerConf.url + "req=" + SinglePlayerServerConf.SecondReq +"&level=0"+ "&country=\"" + country + "\"&city=\"" + city + "\"", { response ->
            val reply = JSONObject(response.toString())

            val fCountry1 = reply!!.get("fCountry1")
            val fCity1 = reply!!.get("fCity1")
            val fCountry2 = reply!!.get("fCountry2")
            val fCity2 = reply!!.get("fCity2")
            val fCountry3 = reply!!.get("fCountry3")
            val fCity3 = reply!!.get("fCity3")

            Log.i("myTag", "country: " + fCountry1 + ", city: " + fCity1 + "\n" +
                    "country: " + fCountry2 + ", city: " + fCity2 + "\n" +
                    "country: " + fCountry3 + ", city: " + fCity3)

            //save everything on resource
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
            val editor = sharedPref?.edit()
            editor?.putString("fCountry1", fCountry1.toString())
            editor?.putString("fCity1", fCity1.toString())
            editor?.putString("fCountry2", fCountry2.toString())
            editor?.putString("fCity2", fCity2.toString())
            editor?.putString("fCountry3", fCountry3.toString())
            editor?.putString("fCity3", fCity3.toString())
            editor?.apply()

            findNavController().navigate(R.id.action_optionsFragment_to_questionCountryFragment)

        }, { error: VolleyError? ->
            Log.i("info", "Polling: " + error.toString())
        })
        stringRequest.setRetryPolicy(DefaultRetryPolicy(
                20 * 1000,  //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        queue?.add(stringRequest)
    }
}