package com.example.mapsproject.Multiplayer.Fragment

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
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Configuration.MultiPlayerServerConf.Companion.queue
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.Multiplayer.MultiplayerActivity
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity
import org.json.JSONObject

class PollingNewLevelFragment: Fragment() {
    lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MultiplayerActivity).findViewById<TextView>(R.id.num_levels).setVisibility(View.VISIBLE)
        (activity as MultiplayerActivity).findViewById<TextView>(R.id.curr_lev).setVisibility(View.VISIBLE)
        (activity as MultiplayerActivity).findViewById<TextView>(R.id.curr_lev).setText(MultiPlayerServerConf.played_levels.toString()+"/"+MultiPlayerServerConf.levels.toString())
        (activity as MultiplayerActivity).findViewById<TextView>(R.id.score).setVisibility(View.VISIBLE)
        (activity as MultiplayerActivity).findViewById<TextView>(R.id.curr_score).setVisibility(View.VISIBLE)
        //(activity as MultiplayerActivity).findViewById<TextView>(R.id.curr_score).setText(MultiPlayerServerConf.score.toString())
        (activity as MultiplayerActivity).findViewById<TextView>(R.id.num_players).setVisibility(View.VISIBLE)
        (activity as MultiplayerActivity).findViewById<TextView>(R.id.players).setVisibility(View.VISIBLE)
        if(MultiPlayerServerConf.randomVar!=0 && MultiPlayerServerConf.player_id>0){
            (activity as MultiplayerActivity).findViewById<TextView>(R.id.num_players).setText("2")
        }
        else{
            (activity as MultiplayerActivity).findViewById<TextView>(R.id.num_players).setText(MultiPlayerServerConf.num_players.toString())
        }
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
        rootView =  inflater.inflate(R.layout.fragment_loading_view, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //if(MultiPlayerServerConf.game_id>0) getNumPlayers()
        object : Thread() {
            override fun run() {
                super.run()
                Handler(Looper.getMainLooper()).postDelayed({ poolNewLevel() }, MultiPlayerServerConf.pollingPeriod)
            }
        }.start()

    }

    private fun poolNewLevel() {
        val l:Int
        if(MultiPlayerServerConf.played_levels<=3)
            l= MultiPlayerServerConf.played_levels
        else
            l=3

        /*if(MultiPlayerServerConf.player_id>0 && MultiPlayerServerConf.played_levels==0)*/ getNumAndNamePlayers()

        Log.i("myTag","request: "+ MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.startLevelReq+
                "&player_id="+ MultiPlayerServerConf.player_id+"&game_id="+ MultiPlayerServerConf.game_id+"&level="+l)
        val stringRequest = StringRequest(
                Request.Method.GET,   MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.startLevelReq+
                "&player_id="+ MultiPlayerServerConf.player_id+"&game_id="+ MultiPlayerServerConf.game_id+"&level="+l,{
            response->
            val reply = JSONObject(response.toString())
            val waiting = reply!!.getBoolean("error")
            val msg = reply!!.getString("msg")
            Log.i("myTag","waiting: "+waiting+", msg: "+msg)

            if (!waiting){

                val country = reply!!.getString("Country")
                val city = reply!!.getString("City")
                val fCountry1 = reply!!.getString("fCountry1")
                val fCity1 = reply!!.getString("fCity1")
                val fCountry2 = reply!!.getString("fCountry2")
                val fCity2 = reply!!.getString("fCity2")
                val fCountry3 = reply!!.getString("fCountry3")
                val fCity3 = reply!!.getString("fCity3")

                val lat = reply!!.getDouble("lat")
                val long = reply!!.getDouble("long")

                Log.i("myTag","country: "+country+", city: "+city+
                                        ", fCountry1: "+fCountry1+", fCity1: "+fCity1+
                                        ", fCountry2: "+fCountry2+", fCity2: "+fCity2+
                                        ", fCountry3: "+fCountry3+", fCity3: "+fCity3)

                //save everything on resource
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
                val editor = sharedPref?.edit()
                editor?.putString("country", country.toString())
                editor?.putString("city", city.toString())
                editor?.putString("fCountry1", fCountry1.toString())
                editor?.putString("fCity1", fCity1.toString())
                editor?.putString("fCountry2", fCountry2.toString())
                editor?.putString("fCity2", fCity2.toString())
                editor?.putString("fCountry3", fCountry3.toString())
                editor?.putString("fCity3", fCity3.toString())
                editor?.putFloat("lat", lat.toFloat())
                editor?.putFloat("long", long.toFloat())
                editor?.apply()

                MultiPlayerServerConf.queue?.cancelAll(activity)
                findNavController().navigate(R.id.action_pollingNewLevelFragment_to_mapFragmentMP)


            }
            else
                Handler(Looper.getMainLooper()).postDelayed({poolNewLevel()}, MultiPlayerServerConf.pollingPeriod)

        },{ error: VolleyError? ->
            Log.i("info", "Polling: " + error.toString())
            Toast.makeText(activity,"Error:" + error.toString(), Toast.LENGTH_SHORT)
            findNavController().navigate(R.id.action_pollingNewLevelFragment_to_startFragmentMP)
        })
        stringRequest.setRetryPolicy(DefaultRetryPolicy(
                20 * 1000,  //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        queue?.add(stringRequest)

    }
    private  fun getNumAndNamePlayers(){
        Log.i("myTag","request: "+ MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.waitPlayerReq+
                "&game_id="+MultiPlayerServerConf.game_id)
        val stringRequest = StringRequest(
                Request.Method.GET,  MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.waitPlayerReq+
                "&game_id="+MultiPlayerServerConf.game_id,{
            response->
            val reply = JSONObject(response.toString())
            if(MultiPlayerServerConf.player_id>0 && MultiPlayerServerConf.played_levels==0){
                val players = reply.getInt("num_sync_pl") +1
                Log.i("myTag","number of players: "+players)
                MultiPlayerServerConf.num_players = players
                if(activity!= null) (activity as MultiplayerActivity).findViewById<TextView>(R.id.num_players).setText(MultiPlayerServerConf.num_players.toString())
            }
            val name_players = reply.getString("name_players")
            Log.i("myTag","name of players: "+name_players)
            //rootView.findViewById<TextView>(R.id.num_players).text = players.toString()
            MultiPlayerServerConf.name_players = name_players
        },{ error: VolleyError? ->
            Log.i("info", "Polling: " + error.toString())
            Toast.makeText(activity,"Error:" + error.toString(), Toast.LENGTH_SHORT)
        })
       queue?.add(stringRequest)
    }


}