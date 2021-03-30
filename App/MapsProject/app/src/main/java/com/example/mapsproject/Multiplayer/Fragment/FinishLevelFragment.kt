package com.example.mapsproject.Multiplayer.Fragment

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mapsproject.Account.Account
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.Multiplayer.MultiplayerActivity
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity
import org.json.JSONObject

class FinishLevelFragment : Fragment() {

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
        object : Thread() {
            override fun run(){
                super.run()
                Handler(Looper.getMainLooper()).postDelayed({finishLevel()}, MultiPlayerServerConf.pollingPeriod)
                if(SinglePlayerServerConf.soundOn) {
                    val mysong = MediaPlayer.create(context, R.raw.next_level)
                    mysong.start()
                }
            }
        }.start()
    }

    private fun finishLevel() {
        Log.i("myTag","request: "+ MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.finishLevelReq+
                "&user_name="+ Account.getUserName() +"&game_id="+ MultiPlayerServerConf.game_id+"&score="+MultiPlayerServerConf.score)
        val stringRequest = StringRequest(
                Request.Method.GET,   MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.finishLevelReq+
                "&user_name="+ Account.getUserName() +"&game_id="+ MultiPlayerServerConf.game_id+"&score="+MultiPlayerServerConf.score, { response ->
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