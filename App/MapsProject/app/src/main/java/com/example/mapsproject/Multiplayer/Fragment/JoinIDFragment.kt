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

    lateinit var rootView:View

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView =  inflater.inflate(R.layout.fragment_join_id, container, false)
        rootView.findViewById<Button>(R.id.submit_game_id_btn).setOnClickListener { view ->
            val game_id = rootView.findViewById<EditText>(R.id.game_id).text.toString().toInt()

            if(game_id== null)
            {
                Toast.makeText(activity,"insert a game_id",Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            MultiPlayerServerConf.game_id =game_id

            findNavController().navigate(R.id.action_joinIDFragment_to_waitingJoinFragment)

        }
        return rootView
    }

}