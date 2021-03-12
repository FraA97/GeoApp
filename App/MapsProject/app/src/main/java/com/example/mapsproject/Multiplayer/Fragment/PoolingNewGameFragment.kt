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
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity
import org.json.JSONObject

class PoolingNewGameFragment: Fragment() {

    var starting = false

    lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MultiPlayerServerConf.queue = Volley.newRequestQueue(context)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_number_of_player, container, false)
        rootView.findViewById<Button>(R.id.share_game_btn).setOnClickListener { view->
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, "join my game on GeoApp, this is my game id: "+MultiPlayerServerConf.game_id)
            sendIntent.type = "text/plain"
            startActivity(sendIntent)

        }
        rootView.findViewById<Button>(R.id.start_multiplayer_btn).setOnClickListener { view->
           starting = true
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //poolNewGame()
          Handler(Looper.getMainLooper()).postDelayed({poolNewGame()},MultiPlayerServerConf.pollingPeriod)
    }

    private fun poolNewGame() {
        Log.i("myTag","request: "+ MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.waitPlayerReq+
                        "&player_id="+MultiPlayerServerConf.player_id+"&game_id="+MultiPlayerServerConf.game_id)
        val stringRequest = StringRequest(
                Request.Method.GET,  MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.waitPlayerReq+
                "&player_id="+MultiPlayerServerConf.player_id+"&game_id="+MultiPlayerServerConf.game_id,{
            response->
            val reply = JSONObject(response.toString())
            val players = reply.getInt("num_sync_pl") +1

            Log.i("myTag","number of players: "+players)
            rootView.findViewById<TextView>(R.id.players_number).setText(players.toString())

            if(!starting) {
                Handler(Looper.getMainLooper()).postDelayed({ poolNewGame() }, MultiPlayerServerConf.pollingPeriod)
            }
            else
                findNavController().navigate(R.id.action_poolingNewGameFragment_to_pollingNewLevelFragment)
        },{ error: VolleyError? ->
            Log.i("info", "Polling: " + error.toString())
        })
        MultiPlayerServerConf.queue?.add(stringRequest)

    }
}