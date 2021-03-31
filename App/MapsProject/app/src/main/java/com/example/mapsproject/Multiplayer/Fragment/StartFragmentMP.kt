package com.example.mapsproject.Multiplayer.Fragment

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Multiplayer.MultiplayerActivity
import com.example.mapsproject.R

class StartFragmentMP: Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_start_game_multiplayer, container, false)
        MultiPlayerServerConf.score=0
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.start_new_btn).setOnClickListener { view ->
            findNavController().navigate(R.id.action_startFragmentMP_to_newGameFragment)

        }

        view.findViewById<Button>(R.id.join_btn).setOnClickListener { view ->
            findNavController().navigate(R.id.action_startFragmentMP_to_joinIDFragment)

        }

        view.findViewById<Button>(R.id.button5).setOnClickListener { view->
            findNavController().navigate(R.id.action_startFragmentMP_to_joinRandomFragment)
        }
    }


}