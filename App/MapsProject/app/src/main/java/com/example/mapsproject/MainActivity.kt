package com.example.mapsproject

import android.app.AlertDialog
import android.app.Application
import android.content.ClipData
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mapsproject.Account.Account.getUserName
import com.example.mapsproject.Account.Account.logOut
import com.example.mapsproject.Account.Account.signIn
import com.example.mapsproject.Account.Account.user
import com.example.mapsproject.Account.LoginActivity
import com.example.mapsproject.Account.SignupActivity
import com.example.mapsproject.Configuration.MultiPlayerServerConf
import com.example.mapsproject.Settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("myTag",Integer.valueOf(android.os.Build.VERSION.SDK).toString())
        super.onCreate(savedInstanceState)
        val myLocale = Locale(MultiPlayerServerConf.language)
        val res: Resources = resources
        val dm: DisplayMetrics = res.getDisplayMetrics()
        val conf: Configuration = res.getConfiguration()
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)

        setContentView(R.layout.activity_main)
        //launch login activity
        findViewById<Button>(R.id.log_in_btn).setOnClickListener { view ->
            if(user!= null){
                logOut(applicationContext)
            }
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            /*val intent = Intent(this, StartGameActivity::class.java)
            startActivity(intent)
            finish()*/
        }

        //launch signup activity
        findViewById<Button>(R.id.sign_up_btn).setOnClickListener { view ->
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }


        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        //if email and password are saved, try firebase login
        if(sharedPref?.contains(R.string.email_key.toString()) == true) {
            if (sharedPref?.contains(R.string.password_key.toString()) == true) {
                //get email and password
                val email = sharedPref?.getString(R.string.email_key.toString(), "")
                val password = sharedPref?.getString(R.string.password_key.toString(), "")
                Log.i("myTag", "try login with email: " + email + ", pws: " + password)
                signIn(email!!, password!!, false, applicationContext)
                val linearLayout = findViewById<LinearLayout>(R.id.layout_logged)
                Handler(Looper.getMainLooper()).postDelayed({updateUI(linearLayout)}, 1000)
            }
        }

    }


    private fun updateUI(layout: LinearLayout) {
        if(user!=null) {
            layout.visibility= View.VISIBLE
            layout.findViewById<TextView>(R.id.tv_main_activity).visibility= View.VISIBLE
            layout.findViewById<TextView>(R.id.logged_user_name).setText(getUserName())
            layout.findViewById<TextView>(R.id.log_in_tv).setOnClickListener{
                startActivity(Intent(this,StartGameActivity::class.java))
            }
            layout.findViewById<TextView>(R.id.log_out_tv).setOnClickListener {
                logOut(applicationContext)
                layout.visibility = View.GONE
            }
        }
        else
            Handler(Looper.getMainLooper()).postDelayed({updateUI(layout)}, 1000)
    }

    //inflate menu_main
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_start , menu)
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

    override fun onResume() {
        super.onResume()
        val myLocale = Locale(MultiPlayerServerConf.language)
        val res: Resources = resources
        val dm: DisplayMetrics = res.getDisplayMetrics()
        val conf: Configuration = res.getConfiguration()
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.title_close_app))
            .setMessage(R.string.msg_close_app)
            .setPositiveButton(android.R.string.yes) { dialog, which ->
                finish()
                val a = Intent(Intent.ACTION_MAIN)
                a.addCategory(Intent.CATEGORY_HOME)
                a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(a)
            }
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
        /* val intent = Intent(this, MainActivity::class.java)
         startActivity(intent)*/

    }


}