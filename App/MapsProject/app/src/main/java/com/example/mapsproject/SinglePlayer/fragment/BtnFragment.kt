package com.example.mapsproject.SinglePlayer.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.R

class BtnFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_btn, container, false)

        //getCurrentScore
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
        val defaultValue = resources.getInteger(R.integer.saved_score_default_key)
        val score = sharedPref?.getInt(getString(R.string.current_score_key), defaultValue)

        //print Score
        rootView.findViewById<TextView>(R.id.current_score_tv).setText(score.toString())

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.start_game_btn).setOnClickListener{ view->
            if(SinglePlayerServerConf.soundOn) {
                val mysong = MediaPlayer.create(context, R.raw.next_level)
                mysong.start()
            }
            findNavController().navigate(R.id.action_btnFragment_to_latLongFragment)
        }
    }
}