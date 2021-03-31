package com.example.mapsproject.Multiplayer.Fragment

import android.app.AlertDialog
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
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Multiplayer.MultiplayerActivity
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity
import org.json.JSONObject

class PollingFinishLevelFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MultiPlayerServerConf.queue = Volley.newRequestQueue(context)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(context)
                        .setTitle(getString(R.string.title_back_press))
                        .setMessage(R.string.msg_back_press)
                        .setPositiveButton(android.R.string.yes) { dialog, which ->
                            (activity as MultiplayerActivity).interruptGame()
                            val i = Intent(activity, StartGameActivity::class.java)
                            //(activity as MultiplayerActivity).finish()
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
                val num_pl_left = reply!!.getInt("num_pl_left")
                Log.i("myTag","num_pl_left: "+num_pl_left)
                if(num_pl_left>0 && !MultiPlayerServerConf.wantToPlay){
                    //show popup
                    if(MultiPlayerServerConf.player_id > 0){
                        AlertDialog.Builder(context)
                                .setTitle(getString(R.string.title_end_g))
                                //.setMessage(R.string.msg_end_g)
                                .setPositiveButton(R.string.exit) { dialog, which ->
                                    MultiPlayerServerConf.wantToPlay = false
                                    (activity as MultiplayerActivity).interruptGame()
                                    val i = Intent(activity, StartGameActivity::class.java)
                                    startActivity(i)
                                }
                                /*.setNegativeButton(R.string.n) { dialog, which ->
                                    (activity as MultiplayerActivity).interruptGame()
                                    val i = Intent(activity, StartGameActivity::class.java)
                                    // finish()
                                    startActivity(i)
                                }*/
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show()
                    }
                    else {
                        AlertDialog.Builder(context)
                                .setTitle(getString(R.string.title_pl_left))
                                .setMessage(R.string.msg_pl_left)
                                .setPositiveButton(R.string.y) { dialog, which ->
                                    MultiPlayerServerConf.wantToPlay = true
                                    MultiPlayerServerConf.num_players= MultiPlayerServerConf.num_players-num_pl_left
                                    Log.i("myTag","num_players: "+MultiPlayerServerConf.num_players)
                                    if (MultiPlayerServerConf.played_levels <= MultiPlayerServerConf.levels) {
                                        findNavController().navigate(R.id.action_pollingFinishLevelFragment_to_pollingNewLevelFragment2)
                                    } else  {
                                        findNavController().navigate(R.id.action_pollingFinishLevelFragment_to_endGameFragmentMp)
                                    }
                                }
                                .setNegativeButton(R.string.n) { dialog, which ->
                                    (activity as MultiplayerActivity).interruptGame()
                                    val i = Intent(activity, StartGameActivity::class.java)
                                    // finish()
                                    startActivity(i)
                                }
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show()
                    }
                }
                if (MultiPlayerServerConf.played_levels <= MultiPlayerServerConf.levels && (num_pl_left==0 || MultiPlayerServerConf.wantToPlay)) {
                    findNavController().navigate(R.id.action_pollingFinishLevelFragment_to_pollingNewLevelFragment2)
                } else if(MultiPlayerServerConf.played_levels > MultiPlayerServerConf.levels && (num_pl_left==0 || MultiPlayerServerConf.wantToPlay)) {
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