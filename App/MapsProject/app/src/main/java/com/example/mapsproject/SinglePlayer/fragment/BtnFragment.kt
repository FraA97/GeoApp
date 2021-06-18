package com.example.mapsproject.SinglePlayer.fragment

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity

class BtnFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                AlertDialog.Builder(context)
                        .setTitle(getString(R.string.title_back_press))
                        .setMessage(R.string.msg_back_press)
                        .setPositiveButton(android.R.string.yes) { dialog, which ->
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
        val rootView = inflater.inflate(R.layout.fragment_btn, container, false)

        //getCurrentScore
        //val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
        //val defaultValue = resources.getInteger(R.integer.saved_score_default_key)
        //val score = sharedPref?.getInt(getString(R.string.current_score_key), defaultValue)

        //print Score
        //rootView.findViewById<TextView>(R.id.current_score_tv).setText(score.toString())

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