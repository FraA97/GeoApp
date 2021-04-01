package com.example.mapsproject

import android.content.ClipData
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
import androidx.appcompat.app.AppCompatActivity
import com.example.mapsproject.Account.Account.signIn
import com.example.mapsproject.Account.Account.user
import com.example.mapsproject.Account.LoginActivity
import com.example.mapsproject.Account.SignupActivity
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Settings.Settings
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myLocale = Locale(MultiPlayerServerConf.language)
        val res: Resources = resources
        val dm: DisplayMetrics = res.getDisplayMetrics()
        val conf: Configuration = res.getConfiguration()
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)


        //if email and password are saved, try firebase login
        if(sharedPref?.contains("email") == true && sharedPref?.contains("password")) {
            //get email and password
            if(user != null){
                Log.i("myTag", "user didn't loged out")
                val intent = Intent(this, StartGameActivity::class.java)
                startActivity(intent)
            }
            else {

                val email = sharedPref?.getString("email", "")
                val password = sharedPref?.getString("password", "")

                Log.i("myTag", "try login with email: " + email + ", pws: " + password)

                signIn(email!!, password!!, false, this)
                if (user != null) {
                    val intent = Intent(this, StartGameActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Log.i("myTag", "unable to login")
                }
            }
        }
        else{
            Log.i("myTag", "no password or email stored")
        }

        setContentView(R.layout.activity_main)

        //launch login activity
        findViewById<Button>(R.id.log_in_btn).setOnClickListener{ view->
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            /*val intent = Intent(this, StartGameActivity::class.java)
            startActivity(intent)
            finish()*/
        }

        //launch signup activity
        findViewById<Button>(R.id.sign_up_btn).setOnClickListener{ view->
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    //inflate menu_main
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main , menu)
       // menu?.findItem(1)?.setVisible(false)
        var i = 0
        while ( i < menu?.size() ?: 4){
            if(i!=1) {
                menu?.getItem(i)?.setVisible(false)
            }
            i += 1
        }
        //this.findViewById<>(R.id.action_leaderboard).setVisibility(View.INVISIBLE)
        return true
    }

    //handle menu actions
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
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