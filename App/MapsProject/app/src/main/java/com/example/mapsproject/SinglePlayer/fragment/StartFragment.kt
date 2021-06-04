package com.example.mapsproject.SinglePlayer.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mapsproject.Account.Account.getHighScore
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.MainActivity
import com.example.mapsproject.R
import com.example.mapsproject.Settings.MyCustomDialog
import com.example.mapsproject.Settings.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartFragment: Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        if(SinglePlayerServerConf.level > 1)
            findNavController().navigate(R.id.action_startFragment_to_latLongFragment)

        val rootView =  inflater.inflate(R.layout.fragment_start_game, container, false)

        //get High Score
        val hstv = rootView.findViewById<TextView>(R.id.high_score_tv)
        updateHighScore(hstv)
        rootView.findViewById<TextView>(R.id.levels_tv).setText(SinglePlayerServerConf.sets.toString())


        //set current score to 0
        SinglePlayerServerConf.score = 0
        SinglePlayerServerConf.level = 1

        rootView.findViewById<LinearLayout>(R.id.lin_lay_st_act)?.setOnClickListener { view ->
            val intent = Intent(activity, Settings::class.java)
            startActivity(intent)
            //finish()
        }



        return rootView
    }

    private fun updateHighScore(tv: TextView) {
        var hs = 100000
        CoroutineScope(Dispatchers.Main).launch{
            withContext(Dispatchers.Default) {
                 hs = getHighScore()
            }
            tv.setText(hs.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.start_game_btn).setOnClickListener { view ->
            findNavController().navigate(R.id.action_startFragment_to_latLongFragment)

        }
    }
}