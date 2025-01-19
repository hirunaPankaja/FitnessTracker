package com.example.fitnesstracker

import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Step4 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.step4)

        // Find the back arrow and set click listener
        val backArrow = findViewById<ImageView>(R.id.backArrow)
        backArrow.setOnClickListener {
            onBackPressed() // Handle back navigation
        }

        // Find the SeekBar and TextView for weight
        val weightSeekBar = findViewById<SeekBar>(R.id.weightSeekBar)
        val weightDisplay = findViewById<TextView>(R.id.weightDisplay)

        // Set an OnSeekBarChangeListener to update the weight display as the SeekBar is moved
        weightSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Update weight display in kg based on progress
                weightDisplay.text = "${progress} kg"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}
