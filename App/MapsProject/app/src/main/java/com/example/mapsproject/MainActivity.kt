package com.example.mapsproject

import android.R.id.message
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.single_player_btn).setOnClickListener{ view->
            val intent = Intent(this,MainSinglePlayerActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.multiplayer_btn).setOnClickListener{ view->
            val intent = Intent(this,MainMultiplayerActivity::class.java)
            startActivity(intent)
        }

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPref?.edit()
        //IF HIGH SOCRE DOESN'T EXIST set to 0
        if( !sharedPref?.contains(R.string.high_score_key.toString())!!){
            Log.i("myTag","hs didn't exist")
            editor?.putInt(getString(R.string.high_score_key), 2)
            editor?.apply()
        }

    }

}