package com.example.mapsproject.Multiplayer.Fragment

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
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
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mapsproject.Account.Account
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Configuration.MultiPlayerServerConf.Companion.queue
import com.example.mapsproject.Multiplayer.MultiplayerActivity
import com.example.mapsproject.R
import com.example.mapsproject.SinglePlayer.SingleplayerActivity
import com.example.mapsproject.StartGameActivity
import org.json.JSONObject

class WaitingNewGameFragment:Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MultiplayerActivity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED) //block screen orientation
        queue = Volley.newRequestQueue(context)

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

        Handler(Looper.getMainLooper()).postDelayed({createGame()},MultiPlayerServerConf.pollingPeriod)
    }

    private fun createGame() {
        Log.i("myTag","request: "+ MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.startGameReq+"&num_levels="+MultiPlayerServerConf.levels+"&user_name="+ Account.getUserName())
        val stringRequest = StringRequest(
                Request.Method.GET,  MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.startGameReq+"&num_levels="+MultiPlayerServerConf.levels+"&user_name="+ Account.getUserName(),{
            response->
            val reply = JSONObject(response.toString())
            val error = reply!!.getBoolean("error")
            val msg = reply!!.getString("msg")
            if(!error){
                MultiPlayerServerConf.game_id = reply!!.getInt("game_id")
                MultiPlayerServerConf.player_id = reply!!.getInt("player_id")

                Log.i("myTag","game id: "+MultiPlayerServerConf.game_id+"; player id: "+MultiPlayerServerConf.player_id+"; num_levels: "+MultiPlayerServerConf.levels)
                findNavController().navigate(R.id.action_waitingNewGameFragment_to_poolingNewGameFragment)
            }
            else{
                Toast.makeText(activity,"ERROR: "+msg,Toast.LENGTH_SHORT).show()
                Log.i("myTag","Error:" + msg)
            }

        },{ error: VolleyError? ->
            Log.i("info", "Polling: " + error.toString())
            Toast.makeText(activity,"Error:" + error.toString(), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_waitingNewGameFragment_to_newGameFragment)
        })
        queue?.add(stringRequest)

    }


}
