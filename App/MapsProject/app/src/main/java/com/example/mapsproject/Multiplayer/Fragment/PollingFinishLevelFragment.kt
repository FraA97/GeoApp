package com.example.mapsproject.Multiplayer.Fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity
import org.json.JSONObject

class PollingFinishLevelFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MultiPlayerServerConf.queue = Volley.newRequestQueue(context)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_loading_view, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        object : Thread() {
            override fun run(){
                super.run()
                Handler(Looper.getMainLooper()).postDelayed({poolFinishLevel()}, MultiPlayerServerConf.pollingPeriod)
            }
        }.start()
    }

    private fun poolFinishLevel() {
        Log.i("myTag","request: "+ MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.waitLevelReq+
                "&player_id="+ MultiPlayerServerConf.player_id+"&game_id="+ MultiPlayerServerConf.game_id)
        val stringRequest = StringRequest(
                Request.Method.GET,  MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.waitLevelReq+
                "&player_id="+ MultiPlayerServerConf.player_id+"&game_id="+ MultiPlayerServerConf.game_id,{
            response->
            val reply = JSONObject(response.toString())
            val waiting = reply!!.getBoolean("waiting")
            Log.i("myTag","waiting: "+waiting)

            if (!waiting) {
                MultiPlayerServerConf.queue?.cancelAll(activity)
                MultiPlayerServerConf.played_levels += 1
                MultiPlayerServerConf.totalScore = reply!!.getJSONObject("total_score")
                Log.i("myTag","total_score: "+MultiPlayerServerConf.totalScore)
                //Log.i("myTag","class:"+MultiPlayerServerConf.totalScore["0"].javaClass.name)
                if (MultiPlayerServerConf.played_levels <= MultiPlayerServerConf.levels) {
                    findNavController().navigate(R.id.action_pollingFinishLevelFragment_to_pollingNewLevelFragment2)
                } else {
                    findNavController().navigate(R.id.action_pollingFinishLevelFragment_to_endGameFragmentMp)
                }
            }
            else
                Handler(Looper.getMainLooper()).postDelayed({poolFinishLevel()}, MultiPlayerServerConf.pollingPeriod)

        },{ error: VolleyError? ->
            Log.i("info", "Polling: " + error.toString())
        })
        MultiPlayerServerConf.queue?.add(stringRequest)

    }
}