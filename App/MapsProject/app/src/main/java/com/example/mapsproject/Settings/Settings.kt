package com.example.mapsproject.Settings

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import com.example.mapsproject.Account.Account
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.MainActivity
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial
import java.util.*


class Settings:Activity(), AdapterView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sound = findViewById<SwitchMaterial>(R.id.soundSetting)
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

        var items= arrayOf("")
        val dropdown = findViewById<Spinner>(R.id.language)

        if(MultiPlayerServerConf.language=="en") {
            items = arrayOf("choose language","English", "Italian")
        }
        else{
            items = arrayOf("scegli la lingua","Inglese", "Italiano")
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)

        dropdown.adapter = adapter


        //val dropdow = findViewById<Spinner>(R.id.language)
        dropdown.onItemSelectedListener = this

        //listener on back button

        this.findViewById<FloatingActionButton>(R.id.turn_back).setOnClickListener {
            val lev = findViewById<EditText>(R.id.levels_int).text
            if(lev!= null){
                val levels = lev.toString()
                if(levels != "") {
                    SinglePlayerServerConf.sets = levels.toInt()
                }
            }
            if(Account.getUserID()==""){
                val i = Intent(this, MainActivity::class.java)
                finish()
                startActivity(i)
            }
            else{
                val i = Intent(this, StartGameActivity::class.java)
                finish()
                startActivity(i)
            }
        }

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        if(parent.getItemAtPosition(pos)=="scegli la lingua" || parent.getItemAtPosition(pos)=="choose language"){
        }
        else if(parent.getItemAtPosition(pos)=="English" || parent.getItemAtPosition(pos)=="Inglese"){
            Toast.makeText(this, "Setted English Language", LENGTH_SHORT).show()
            MultiPlayerServerConf.language = "en"
            setLocale("en")
        }
        else if(parent.getItemAtPosition(pos)=="Italian" || parent.getItemAtPosition(pos)=="Italiano" ){
            MultiPlayerServerConf.language = "it"
            Toast.makeText(this, "Impostata la lingua italiana", LENGTH_SHORT).show()
            setLocale("it")
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }



    fun setLocale(lang: String?) {
        val myLocale = Locale(lang)
        val res: Resources = resources
        val dm: DisplayMetrics = res.getDisplayMetrics()
        val conf: Configuration = res.getConfiguration()
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
        val refreshSettings = Intent(this, Settings::class.java)
        finish()
        startActivity(refreshSettings)
        /*val refreshMain = Intent( this, StartGameActivity::class.java)
        finish()
        startActivity(refreshMain)*/
    }

    override fun onBackPressed() {
        val lev = findViewById<EditText>(R.id.levels_int).text
        if(lev!= null){
            val levels = lev.toString()
            if(levels != "") {
                SinglePlayerServerConf.sets = levels.toInt()
            }
        }
        if(Account.getUserID()==""){
            val i = Intent(this, MainActivity::class.java)
            finish()
            startActivity(i)
        }
        else{
            val i = Intent(this, StartGameActivity::class.java)
            finish()
            startActivity(i)
        }


    }

}