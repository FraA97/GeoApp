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
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity
import java.util.*


class Settings:Activity(), AdapterView.OnItemSelectedListener {
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

       /* val btnEnglish = findViewById<View>(R.id.en_lan_butt)
        btnEnglish.setOnClickListener { view ->
            Toast.makeText(this, "Setted English Language", LENGTH_SHORT).show()
            MultiPlayerServerConf.language = "en"
            setLocale("en")
                /*val context = LocaleHelper.setLocale(this, "en")
                val resources = context.getResources()*/
                //messageView.setText(resources.getString(R.string.language))
            }

        val btnItalian = findViewById<View>(R.id.it_lan_butt)
        btnItalian.setOnClickListener { view ->
            MultiPlayerServerConf.language = "it"
            Toast.makeText(this, "Impostata la lingua italiana", LENGTH_SHORT).show()
            setLocale("it")
            /*Log.i("mYTag", "changed language in Italian")
              val context = LocaleHelper.setLocale(this, "it")
              val resources = context.getResources()*/
              //messageView.setText(resources.getString(R.string.language))
        }*/

        //get the spinner from the xml.
        //get the spinner from the xml.
        var items= arrayOf("")
        val dropdown = findViewById<Spinner>(R.id.language)

        if(MultiPlayerServerConf.language=="en") {
            items = arrayOf("choose language","English", "Italian")
        }
        else{
            items = arrayOf("scegli la lingua","Inglese", "Italiano")
        }
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        //set the spinners adapter to the previously created one.
        //set the spinners adapter to the previously created one.
        dropdown.adapter = adapter


        //val dropdow = findViewById<Spinner>(R.id.language)
        dropdown.onItemSelectedListener = this

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        /*Log.i("tyTag","SVEGLIAAAAAAAAAAAAAAAAAAAAAA")
        Log.i("tyTag",""+parent.getItemAtPosition(pos))*/
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
        val i = Intent(this, StartGameActivity::class.java)
        finish()
        startActivity(i)

    }

}