package com.example.mapsproject.SinglePlayer

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.example.mapsproject.Account.Account.logOut
import com.example.mapsproject.Account.AccountSettingsActivity
import com.example.mapsproject.Configuration.MultiPlayerServerConf.Companion.queue
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.MainActivity
import com.example.mapsproject.R
import com.example.mapsproject.Settings.LeaderBoard
import com.example.mapsproject.Settings.Settings
import org.json.JSONObject

class SingleplayerActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singleplayer)
        this.findViewById<TextView>(R.id.curr_score_sp).setVisibility(View.INVISIBLE)
        this.findViewById<TextView>(R.id.score_sp).setVisibility(View.INVISIBLE)
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
            finish()

            true
        }

        R.id.action_account_settings->{
            val intent = Intent(this, AccountSettingsActivity::class.java)
            startActivity(intent)
            finish()

            true
        }
        R.id.action_settings->{
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()

            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }




}


