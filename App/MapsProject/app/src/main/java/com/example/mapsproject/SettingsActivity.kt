package com.example.mapsproject

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.mapsproject.Multiplayer.MultiplayerActivity
import com.example.mapsproject.SinglePlayer.SingleplayerActivity

class SettingsActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        findViewById<Button>(R.id.sets_btn).setOnClickListener{ view->
            val sets = findViewById<EditText>(R.id.editTextNumber).text.toString()
            if (sets!= ""){
                val s = sets.toInt()

                val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = sharedPref?.edit()
                editor?.putInt(R.string.sets_key.toString(),s)
            }
        }

    }
}