package com.example.fitnesstracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.R
import com.google.firebase.auth.FirebaseAuth

class login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.login)

        auth = FirebaseAuth.getInstance()
        usernameInput = findViewById(R.id.email_input)
        passwordInput = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val email = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        Log.d("LoginActivity", "Attempting to login with email: $email and password: $password")
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity1::class.java)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                    Log.e("LoginActivity", "Authentication failed: ${task.exception?.message}")
                }
            }
    }
}
