package com.example.mapsproject.Multiplayer.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.R
import org.json.JSONObject

class JoinIDFragment:Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_join_id, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.submit_game_id_btn).setOnClickListener { view ->
            MultiPlayerServerConf.game_id = view.findViewById<EditText>(R.id.game_id_tv).text.toString().toInt()

            Log.i("myTag","request: "+ MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.startGameReq+"&game_id="+MultiPlayerServerConf.game_id)
            val stringRequest = StringRequest(
                    Request.Method.GET,  MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.startGameReq+"&game_id="+MultiPlayerServerConf.game_id,{
                response->
                val reply = JSONObject(response.toString())
                val error = reply!!.getBoolean("error")
                val msg = reply!!.getString("msg")
                if(error){
                    Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show()
                    return@StringRequest
                }
                MultiPlayerServerConf.player_id = reply!!.getInt("player_id")

                Log.i("myTag","game id: "+ MultiPlayerServerConf.game_id+"; player id: "+ MultiPlayerServerConf.player_id)

                findNavController().navigate(R.id.action_joinIDFragment_to_poolingNewGameFragment)

            },{ error: VolleyError? ->
                Log.i("info", "Polling: " + error.toString())
            })
            MultiPlayerServerConf.queue?.add(stringRequest)


            //findNavController().navigate(R.id.)

        }

    }
}