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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Multiplayer.MultiplayerActivity
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity
import org.json.JSONObject

class PoolingNewGameFragment: Fragment() {

    var starting = false
    lateinit var rootView: View

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
        rootView =  inflater.inflate(R.layout.fragment_number_of_player, container, false)
        rootView.findViewById<TextView>(R.id.game_id_tv).setText(MultiPlayerServerConf.game_id.toString())
        rootView.findViewById<Button>(R.id.share_game_btn).setOnClickListener { view->
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            val textMsg =getString(R.string.share_g_id_msg)
            sendIntent.putExtra(Intent.EXTRA_TEXT,  textMsg +" "+ MultiPlayerServerConf.game_id.toString())
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
        object :Thread() {
            override fun run(){
                super.run()
                Handler(Looper.getMainLooper()).postDelayed({poolNewGame()},MultiPlayerServerConf.pollingPeriod)
            }
        }.start()
    }

    private fun poolNewGame() {
        Log.i("myTag","request: "+ MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.waitPlayerReq+
                        "&player_id="+MultiPlayerServerConf.player_id+"&game_id="+MultiPlayerServerConf.game_id+ "   played_levels: " +MultiPlayerServerConf.played_levels)
        val stringRequest = StringRequest(
                Request.Method.GET,  MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.waitPlayerReq+
                "&player_id="+MultiPlayerServerConf.player_id+"&game_id="+MultiPlayerServerConf.game_id,{
            response->
            val reply = JSONObject(response.toString())
            val error = reply.getBoolean("error")

            if(!error) {
                val players = reply.getInt("num_sync_pl") + 1

                Log.i("myTag", "number of players: " + players)
                rootView.findViewById<TextView>(R.id.players_number).text = players.toString()
                MultiPlayerServerConf.num_players = players
                MultiPlayerServerConf.played_levels = 1
                Log.i("myTag", "starting: " + starting.toString())

                if (!starting) {
                    Handler(Looper.getMainLooper()).postDelayed({ poolNewGame() }, MultiPlayerServerConf.pollingPeriod)
                } else
                    findNavController().navigate(R.id.action_poolingNewGameFragment_to_pollingNewLevelFragment)
            }
            else{
                val msg = reply!!.getString("msg")
                Log.i("info", "Error: "+ msg)
            }
        },{ error: VolleyError? ->
            Log.i("info", "Polling: " + error.toString())
            //Toast.makeText(activity,"Error:" + error.toString(), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_poolingNewGameFragment_to_newGameFragment)
        })
        MultiPlayerServerConf.queue?.add(stringRequest)

    }


}
