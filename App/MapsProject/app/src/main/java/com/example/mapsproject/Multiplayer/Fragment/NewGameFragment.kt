package com.example.mapsproject.Multiplayer.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.R

class NewGameFragment:Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_new_game_create, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.create_btn).setOnClickListener { view->
            findNavController().navigate(R.id.action_newGameFragment_to_waitingNewGameFragment)
            /*val l = view.findViewById<EditText>(R.id.levels_input).text.toString().toInt()
            MultiPlayerServerConf.levels =  l*/
        }
    }


}