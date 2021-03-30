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
import android.widget.TextView
import android.widget.Toast
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

class WaitingJoinFragment: Fragment(){

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
                            // finish()
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
        object:Thread(){
            override fun run(){
                super.run()
                Handler(Looper.getMainLooper()).postDelayed({joinGame()}, MultiPlayerServerConf.pollingPeriod)
            }
        }.start()
    }

    private fun joinGame(){
        if(MultiPlayerServerConf.randomVar==0) {
            Log.i("myTag", "request: " + MultiPlayerServerConf.url + "req=" + MultiPlayerServerConf.startGameReq + "&game_id=" + MultiPlayerServerConf.game_id)
            val stringRequest = StringRequest(
                    Request.Method.GET, MultiPlayerServerConf.url + "req=" + MultiPlayerServerConf.startGameReq + "&game_id=" + MultiPlayerServerConf.game_id, { response ->
                val reply = JSONObject(response.toString())
                val error = reply!!.getBoolean("error")
                val msg = reply!!.getString("msg")

                if (error) {
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
                    return@StringRequest
                }
                MultiPlayerServerConf.player_id = reply!!.getInt("player_id")
                MultiPlayerServerConf.levels = reply!!.getInt("levels")


                Log.i("myTag", "game id: " + MultiPlayerServerConf.game_id + "; player id: " + MultiPlayerServerConf.player_id)

                findNavController().navigate(R.id.action_waitingJoinFragment_to_pollingNewLevelFragment)

            }, { error: VolleyError? ->
                Log.i("info", "Polling: " + error.toString())
                Toast.makeText(activity, "Error:" + error.toString(), Toast.LENGTH_SHORT)
                findNavController().navigate(R.id.action_waitingJoinFragment_to_joinFragment)
            })
            MultiPlayerServerConf.queue?.add(stringRequest)
        }
        else{
            findNavController().navigate(R.id.action_waitingJoinFragment_to_pollingNewLevelFragment)
        }
    }



}