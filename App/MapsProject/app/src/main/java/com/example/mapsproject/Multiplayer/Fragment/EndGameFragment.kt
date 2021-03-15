package com.example.mapsproject.Multiplayer.Fragment

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.mapsproject.Configuration.MultiPlayerServerConf.Companion.score
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity

class EndGameFragment: Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_end_singleplayer, container, false)
        rootView.findViewById<Button>(R.id.end_btn).setOnClickListener { view ->
            val i = Intent(activity, StartGameActivity::class.java)
            startActivity(i)
        }

        rootView.findViewById<TextView>(R.id.high_score_end).setVisibility(View.GONE)
        rootView.findViewById<TextView>(R.id.score_end).setText(score.toString())

        return rootView
    }
}