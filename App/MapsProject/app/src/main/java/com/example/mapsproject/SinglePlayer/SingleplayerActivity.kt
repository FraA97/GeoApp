package com.example.mapsproject.SinglePlayer

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mapsproject.Account.Account.logOut
import com.example.mapsproject.Account.AccountSettingsActivity
import com.example.mapsproject.MainActivity
import com.example.mapsproject.R
import com.example.mapsproject.Settings.Settings
import kotlin.concurrent.fixedRateTimer

class SingleplayerActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singleplayer)
        this.findViewById<TextView>(R.id.curr_score_sp).setVisibility(View.INVISIBLE)
        this.findViewById<TextView>(R.id.score_sp).setVisibility(View.INVISIBLE)
    }
    //inflate menu_main
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    //handle menu actions
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.action_logout -> {
            AlertDialog.Builder(this)
                    .setTitle(R.string.title_back_press)
                    .setMessage(R.string.msg_back_press)
                    .setPositiveButton(android.R.string.yes) { dialog, which ->
                        logOut(applicationContext)

                        //show logout with Toast
                        Toast.makeText(this, "LogOut Succesful", Toast.LENGTH_LONG).show()

                        //return to MainActivity
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
            true
        }

        R.id.action_account_settings -> {
            AlertDialog.Builder(this)
                    .setTitle(R.string.title_back_press)
                    .setMessage(R.string.msg_back_press)
                    .setPositiveButton(android.R.string.yes) { dialog, which ->
                        val intent = Intent(this, AccountSettingsActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()


            true
        }
        R.id.action_settings -> {
                AlertDialog.Builder(this)
                        .setTitle(R.string.title_back_press)
                        .setMessage(R.string.msg_back_press)
                        .setPositiveButton(android.R.string.yes) { dialog, which ->
                            val intent = Intent(this, Settings::class.java)
                            finish()
                            startActivity(intent)

                        }
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }




}


