package com.example.mapsproject.SinglePlayer.fragment

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mapsproject.Configuration.SinglePlayerServerConf.Companion.score
import com.example.mapsproject.MainActivity
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity

class EndFragment: Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val mysong = MediaPlayer.create(context,R.raw.effetto_vincita)
        mysong.start()
        val rootView = inflater.inflate(R.layout.fragment_end_singleplayer, container, false)
        rootView.findViewById<Button>(R.id.end_btn).setOnClickListener{view->
            mysong.pause()
            val i =  Intent(activity, StartGameActivity::class.java)
            startActivity(i)
        }

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = sharedPref?.edit()
        var highscore = sharedPref?.getInt(getString(R.string.high_score_key), 0)
        if(score!! > highscore!!){
            editor?.putInt(getString(R.string.high_score_key), score)
            highscore = score
        }

        rootView.findViewById<TextView>(R.id.high_score_end).setText(highscore.toString())
        rootView.findViewById<TextView>(R.id.score_end).setText(score.toString())

        return rootView
    }
}