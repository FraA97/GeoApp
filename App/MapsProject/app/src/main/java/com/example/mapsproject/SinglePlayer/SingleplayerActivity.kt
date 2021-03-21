package com.example.mapsproject.SinglePlayer

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mapsproject.Account.Account.logOut
import com.example.mapsproject.Account.AccountSettingsActivity
import com.example.mapsproject.MainActivity
import com.example.mapsproject.R

class SingleplayerActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singleplayer)

    }
    //inflate menu_main
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main , menu)
        return true
    }


    //handle menu actions
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.action_logout -> {
            //erase email and password values from Shared preferences
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
            val editor = sharedPref?.edit()
            editor?.remove("email" )
            editor?.remove("password")
            val success = editor?.commit()

            logOut()


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

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

}