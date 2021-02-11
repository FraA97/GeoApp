package com.example.mapsproject.Account

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mapsproject.R
import com.example.mapsproject.StartGameActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class SignupActivity:AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private var checked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()

        //Switch changes value of var checked
        findViewById<Switch>(R.id.switch_su).setOnCheckedChangeListener { buttonView, isChecked ->
            checked = isChecked
        }


        //signup button OnClickListener
        findViewById<Button>(R.id.submit_btn).setOnClickListener { view->

            val email:String = findViewById<EditText>(R.id.email_entry_su).text.toString()
            //check email correspond to regular expression "*@*.*"
            //if not show error via Toast and return
            val regex = getString(R.string.email_regular_expr).toRegex()
            if (!regex.matches(email)) {
                Toast.makeText(this, "Insert Email", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val psw1 = findViewById<EditText>(R.id.psw1_entry_su).text.toString()
            val psw2 = findViewById<EditText>(R.id.psw2_entry_su).text.toString()
            //check passwords are equal
            //if not show error via Toast and return
            if (psw1 =="" || psw2 =="" || psw1 != psw2) {
                Toast.makeText(this, "Passwords Have to Be Identical", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val password = psw1
            //sign up to firebase
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener{ task ->
                //signup compleated
                if(task.isSuccessful){

                   /* val userid = Firebase.auth.currentUser?.uid
                    Log.i("myTag","user id: "+userid)
*/
                    Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()

                    //if checked -> save access credentials
                    if(checked) {
                        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
                        val editor = sharedPref?.edit()
                        editor?.putString("email", email)
                        editor?.putString("password", password)
                        editor?.apply()
                    }

                    //goto Start Activity
                    val intent = Intent(this, StartGameActivity::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    //show error via Toast
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                }
            })
        }

    }
}