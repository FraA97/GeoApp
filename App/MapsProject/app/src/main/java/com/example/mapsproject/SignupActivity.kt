package com.example.mapsproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class SignupActivity:AppCompatActivity() {
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()

        val error = findViewById<TextView>(R.id.error_input_su)
        error.visibility = View.GONE

        findViewById<Button>(R.id.submit_btn).setOnClickListener { view->
            val email:String = findViewById<EditText>(R.id.email_entry_su).text.toString()
            if (email=="") {
                error.setText("email not inserted")
                error.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val psw1 = findViewById<EditText>(R.id.psw1_entry_su).text.toString()
            val psw2 = findViewById<EditText>(R.id.psw2_entry_su).text.toString()
            if (psw1 =="" || psw2 =="" || psw1 != psw2) {
                error.setText("insert the same password twice")
                error.visibility = View.VISIBLE
                return@setOnClickListener
            }

            val password = psw1
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener{ task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, StartGameActivity::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                }
            })
        }

    }
}