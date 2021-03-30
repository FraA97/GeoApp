package com.example.mapsproject.Multiplayer

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import com.example.mapsproject.Settings.Settings
import org.json.JSONObject

class MultiplayerActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiplayer)
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
        R.id.action_settings->{
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
            finish()

            true
        }

        R.id.action_leaderboard->{
            val intent = Intent(this, LeaderBoard::class.java)
            startActivity(intent)
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    fun interruptGame() {
        Log.i("myTag","request: "+ MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.finishLevelReq+
                "&user_name="+ Account.getUserName() +"&game_id="+ MultiPlayerServerConf.game_id+"&interrupt=1")
        val stringRequest = StringRequest(
                Request.Method.GET,   MultiPlayerServerConf.url +"req="+ MultiPlayerServerConf.waitLevelReq+
                "&game_id="+ MultiPlayerServerConf.game_id+"&interrupt=1", { response ->
            val reply = JSONObject(response.toString())
            MultiPlayerServerConf.queue?.cancelAll(this)
            //findNavController().navigate(R.id.action_finishLevelFragment_to_pollingFinishLevelFragment)


        },{ error: VolleyError? ->
            Log.i("info", "Game Stopped: " + error.toString())
        })
       /* stringRequest.setRetryPolicy(DefaultRetryPolicy(
                20 * 1000,  //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))*/
        MultiPlayerServerConf.queue?.add(stringRequest)

    }

}