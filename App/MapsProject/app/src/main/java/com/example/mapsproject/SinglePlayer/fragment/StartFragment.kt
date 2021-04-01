package com.example.mapsproject.SinglePlayer.fragment

import android.media.MediaPlayer
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
import com.example.mapsproject.Account.Account.getHighScore
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.R
import com.example.mapsproject.SinglePlayer.SingleplayerActivity

class StartFragment: Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView =  inflater.inflate(R.layout.fragment_start_game, container, false)

        //get High Score
        val highScore = getHighScore()

        Log.i("myTag", highScore.toString())

        //print highscore
        rootView.findViewById<TextView>(R.id.high_score_tv).setText(highScore.toString())
        rootView.findViewById<TextView>(R.id.levels_tv).setText(SinglePlayerServerConf.sets.toString())


        //set current score to 0
        SinglePlayerServerConf.score = 0
        SinglePlayerServerConf.level = 0




        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.start_game_btn).setOnClickListener { view ->
            findNavController().navigate(R.id.action_startFragment_to_latLongFragment)

        }
    }
}