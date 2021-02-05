package com.example.mapsproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity:AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.psw_forgot).setOnClickListener {
            val email = findViewById<EditText>(R.id.email_entry_fp).text.toString()

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(this, "Unable to send reset mail", Toast.LENGTH_LONG)
                            .show()
                    }
                })
        }
    }
}