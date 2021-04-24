package com.example.mapsproject.Multiplayer

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.example.mapsproject.Account.Account
import com.example.mapsproject.Account.Account.logOut
import com.example.mapsproject.Account.AccountSettingsActivity
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Configuration.SinglePlayerServerConf
import com.example.mapsproject.MainActivity
import com.example.mapsproject.R
import com.example.mapsproject.Settings.LeaderBoard
import com.example.mapsproject.Settings.MyCustomDialog
import com.example.mapsproject.Settings.Settings
import com.example.mapsproject.StartGameActivity
import org.json.JSONObject

class MultiplayerActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplayer)
        this.findViewById<TextView>(R.id.curr_lev).setVisibility(View.INVISIBLE)
        this.findViewById<TextView>(R.id.num_levels).setVisibility(View.INVISIBLE)
        this.findViewById<TextView>(R.id.curr_score).setVisibility(View.INVISIBLE)
        this.findViewById<TextView>(R.id.score).setVisibility(View.INVISIBLE)

        this.findViewById<TextView>(R.id.num_players).setOnClickListener { view ->
            MyCustomDialog().show(supportFragmentManager, "MyCustomFragment")
        }
    }

    //inflate menu_main
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main , menu)
        return true
    }

    //handle menu actions
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.action_logout -> {
            AlertDialog.Builder(this)
                    .setTitle(R.string.title_back_press)
                    .setMessage(R.string.msg_back_press)
                    .setPositiveButton(android.R.string.yes) { dialog, which ->
                        this.interruptGame()
                        logOut(applicationContext)

                        //show logout with Toast
                        Toast.makeText(this, "LogOut Succesful", Toast.LENGTH_LONG).show()

                        //return to MainActivity
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        //finish()
                    }
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()

            true
        }

        R.id.action_account_settings->{
            AlertDialog.Builder(this)
                    .setTitle(R.string.title_back_press)
                    .setMessage(R.string.msg_back_press)
                    .setPositiveButton(android.R.string.yes) { dialog, which ->
                        this.interruptGame()
                        val intent = Intent(this, AccountSettingsActivity::class.java)
                        startActivity(intent)
                        //finish()
                    }
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()

            true
        }
        R.id.action_settings->{
            AlertDialog.Builder(this)
                    .setTitle(R.string.title_back_press)
                    .setMessage(R.string.msg_back_press)
                    .setPositiveButton(android.R.string.yes) { dialog, which ->
                        this.interruptGame()
                        val intent = Intent(this, Settings::class.java)
                        //finish()
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

    fun interruptGame() {
        Log.i("myTag","request: "+ MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.interruptGameReq+
                "&game_id="+ MultiPlayerServerConf.game_id+"&player_id="+ MultiPlayerServerConf.player_id+"&interrupt=1"+"&user_name="+ Account.getUserName())
        MultiPlayerServerConf.played_levels = 0 //reset number of levels
        MultiPlayerServerConf.wantToPlay=false
        val stringRequest = StringRequest(
                Request.Method.GET,   MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.interruptGameReq+
                "&game_id="+ MultiPlayerServerConf.game_id+"&player_id="+ MultiPlayerServerConf.player_id+"&interrupt=1"+"&user_name="+ Account.getUserName(), { response ->
            val reply = JSONObject(response.toString())
            MultiPlayerServerConf.queue?.cancelAll(this)
        },{ error: VolleyError? ->
            Log.i("info", "Error: " + error.toString())
        })
        MultiPlayerServerConf.queue?.add(stringRequest)
    }
}