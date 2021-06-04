package com.example.mapsproject.SinglePlayer.fragment

import android.app.AlertDialog
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
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.Configuration.SinglePlayerServerConf.Companion.FirstReq
import com.example.mapsproject.Configuration.SinglePlayerServerConf.Companion.url
import com.example.mapsproject.R
import com.example.mapsproject.SinglePlayer.SingleplayerActivity
import com.example.mapsproject.StartGameActivity
import org.json.JSONObject


class LatLongFragment: Fragment() {
    var queue : RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as SingleplayerActivity).findViewById<TextView>(R.id.score_sp).setVisibility(View.VISIBLE)
        (activity as SingleplayerActivity).findViewById<TextView>(R.id.curr_score_sp).setVisibility(View.VISIBLE)
        (activity as SingleplayerActivity).findViewById<TextView>(R.id.curr_lev_sp).setVisibility(View.VISIBLE)
        (activity as SingleplayerActivity).findViewById<TextView>(R.id.curr_lev_sp).setText(SinglePlayerServerConf.level.toString()+"/"+ SinglePlayerServerConf.sets.toString())
        (activity as SingleplayerActivity).findViewById<TextView>(R.id.num_levels_sp).setVisibility(View.VISIBLE)

        queue = Volley.newRequestQueue(context)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(context)
                        .setTitle(getString(R.string.title_back_press))
                        .setMessage(R.string.msg_back_press)
                        .setPositiveButton(android.R.string.yes) { dialog, which ->
                            val i = Intent(activity, StartGameActivity::class.java)
                            (activity as SingleplayerActivity).finish()
                            startActivity(i)
                        }
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show()
            }
        })
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_loading_view, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({ createLevel() }, SinglePlayerServerConf.pollingPeriod)
    }

    private fun createLevel() {
        val l:Int
        if(SinglePlayerServerConf.level<=3)
            l=SinglePlayerServerConf.level
        else
            l=3
        Log.i("myTag", "request: " + url + "req=" + FirstReq + "&level=" + l)
        val stringRequest = StringRequest(
                Request.Method.GET, url + "req=" + FirstReq + "&level=" + l, { response ->
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

            Log.i("myTag", "lat: " + lat + ", long: " + long + ", City: " + responseCity + ", Country: " + responseCountry)

            if(MultiPlayerServerConf.touch ==1) {
                (activity as SingleplayerActivity).findViewById<Button>(R.id.ready_button_sp).setVisibility(View.VISIBLE)
                (activity as SingleplayerActivity).findViewById<Button>(R.id.ready_button_sp).background.setAlpha(100)
                MultiPlayerServerConf.touch = 0
                (activity as SingleplayerActivity).findViewById<Button>(R.id.ready_button_sp).setOnClickListener { view ->
                    //if(SinglePlayerServerConf.level == 1)
                        findNavController().navigate(R.id.action_latLongFragment_to_mapFragment)
                    /*else if(SinglePlayerServerConf.level == 2)
                        findNavController().navigate(R.id.action_latLongFragment_to_mapFragment2)
                    else if(SinglePlayerServerConf.level == 3)
                        findNavController().navigate(R.id.action_latLongFragment_to_mapFragment3)*/
                }
            }
            else {
                //if(SinglePlayerServerConf.level == 1)
                    findNavController().navigate(R.id.action_latLongFragment_to_mapFragment)
               /* else if(SinglePlayerServerConf.level == 2)
                    findNavController().navigate(R.id.action_latLongFragment_to_mapFragment2)
                else if(SinglePlayerServerConf.level == 3)
                    findNavController().navigate(R.id.action_latLongFragment_to_mapFragment3)*/
            }

        }, { error: VolleyError? ->
            Log.i("info", "Polling: " + error.toString())
                Handler(Looper.getMainLooper()).postDelayed({ createLevel() }, SinglePlayerServerConf.pollingPeriod)
        })
        queue?.add(stringRequest)
    }

}