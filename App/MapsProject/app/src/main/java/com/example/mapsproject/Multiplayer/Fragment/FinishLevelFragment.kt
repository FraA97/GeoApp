package com.example.mapsproject.Multiplayer.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.R
import org.json.JSONObject

class FinishLevelFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MultiPlayerServerConf.queue = Volley.newRequestQueue(context)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_loading, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //poolNewGame()
        Handler(Looper.getMainLooper()).postDelayed({finishLevel()}, MultiPlayerServerConf.pollingPeriod)
    }

    private fun finishLevel() {
        Log.i("myTag","request: "+ MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.finishLevelReq+
                "&player_id="+ MultiPlayerServerConf.player_id+"&game_id="+ MultiPlayerServerConf.game_id)
        val stringRequest = StringRequest(
                Request.Method.GET,   MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.finishLevelReq+
                "&player_id="+ MultiPlayerServerConf.player_id+"&game_id="+ MultiPlayerServerConf.game_id, { response ->
                    val reply = JSONObject(response.toString())
                    MultiPlayerServerConf.queue?.cancelAll(activity)
                    findNavController().navigate(R.id.action_finishLevelFragment_to_pollingFinishLevelFragment)


                },{ error: VolleyError? ->
                    Log.i("info", "Polling: " + error.toString())
                })
        stringRequest.setRetryPolicy(DefaultRetryPolicy(
                        20 * 1000,  //After the set time elapses the request will timeout
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        MultiPlayerServerConf.queue?.add(stringRequest)
    }
}