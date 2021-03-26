package com.example.mapsproject.Settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Switch
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity

class Settings:Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sound = findViewById<Switch>(R.id.soundSetting)
        if(!SinglePlayerServerConf.soundOn){sound.setChecked(false)}
        sound?.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                MultiPlayerServerConf.soundOn = true
                SinglePlayerServerConf.soundOn = true
            } else {
                MultiPlayerServerConf.soundOn = false
                SinglePlayerServerConf.soundOn = false
            }
        })
    }
    override fun onBackPressed() {
        val i = Intent(this, StartGameActivity::class.java)
        startActivity(i)
    }

}