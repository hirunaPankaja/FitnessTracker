package com.example.fitnesstracker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OTP_verification : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otp_verification)

        val otp1 = findViewById<EditText>(R.id.otp1)
        val otp2 = findViewById<EditText>(R.id.otp2)
        val otp3 = findViewById<EditText>(R.id.otp3)
        val otp4 = findViewById<EditText>(R.id.otp4)
        val verifyButton = findViewById<Button>(R.id.btnVerifyOtp)

        val sentOtp = intent.getIntExtra("otp", -1)

        otp1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                otp2.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        otp2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                otp3.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        otp3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                otp4.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        otp4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                verifyButton.performClick() // Automatically verify OTP when the last digit is entered
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        verifyButton.setOnClickListener {
            val enteredOtp = "${otp1.text}${otp2.text}${otp3.text}${otp4.text}"

            if (enteredOtp == sentOtp.toString()) {
                Toast.makeText(this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show()
                // Navigate to the next screen
            } else {
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

