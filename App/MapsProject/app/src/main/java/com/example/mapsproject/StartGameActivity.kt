package com.example.mapsproject

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mapsproject.Account.Account
import com.example.mapsproject.Account.Account.logOut
import com.example.mapsproject.Account.AccountSettingsActivity
import com.example.mapsproject.Chat.Chat
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Multiplayer.MultiplayerActivity
import com.example.mapsproject.Settings.LeaderBoard
import com.example.mapsproject.Settings.Settings
import com.example.mapsproject.SinglePlayer.SingleplayerActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class StartGameActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myLocale = Locale(MultiPlayerServerConf.language)
        val res: Resources = resources
        val dm: DisplayMetrics = res.getDisplayMetrics()
        val conf: Configuration = res.getConfiguration()
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)

        setContentView(R.layout.activity_start_game)


        findViewById<Button>(R.id.single_player_btn).setOnClickListener{ view->
            val intent = Intent(this, SingleplayerActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.multiplayer_btn).setOnClickListener{ view->
            val intent = Intent(this, MultiplayerActivity::class.java)
            startActivity(intent)
        }

        findViewById<FloatingActionButton>(R.id.chat_fab).setOnClickListener{
            val intent = Intent(this, Chat::class.java)
            startActivity(intent)
        }

        findViewById<FloatingActionButton>(R.id.leaderboard_fab).setOnClickListener{
            val intent = Intent(this, LeaderBoard::class.java)
            startActivity(intent)
        }

    }
    override fun onBackPressed() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)

        //if email and password are not saved, log out
        if((sharedPref?.contains(R.string.email_key.toString()) == false) ||
             (sharedPref?.contains(R.string.password_key.toString()) == false) ){
                //get email and password
                    Log.i("myTag","logging out")
               logOut(applicationContext)
            }
        finish()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        val myLocale = Locale(MultiPlayerServerConf.language)
        val res: Resources = resources
        val dm: DisplayMetrics = res.getDisplayMetrics()
        val conf: Configuration = res.getConfiguration()
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
    }


    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onStop(){
        super.onStop()
    }

    //inflate menu_main
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main , menu)
        return true
    }


    //handle menu actions
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {


        R.id.action_logout -> {
            logOut(applicationContext)
            //show logout with Toast
            Toast.makeText(this, "LogOut Succesful", Toast.LENGTH_LONG).show()

            //return to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            true
        }

        R.id.action_account_settings->{
            val intent = Intent(this, AccountSettingsActivity::class.java)
            startActivity(intent)
            true
        }
        R.id.action_settings->{
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            true
        }


        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}