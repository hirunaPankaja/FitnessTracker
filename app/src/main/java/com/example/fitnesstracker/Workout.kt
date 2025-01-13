package com.example.fitnesstracker

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import android.widget.TextView

class Workout : AppCompatActivity() {

    private var timer: CountDownTimer? = null
    private var timeLeftInSeconds: Long = 30 // Start with 30 seconds
    private val maxTimeInSeconds: Long = 30 // 30 seconds is the max time
    private lateinit var startButton: MaterialButton
    private lateinit var pauseButton: MaterialButton
    private lateinit var resetButton: MaterialButton
    private lateinit var timerDisplay: TextView
    private lateinit var circularProgressIndicator: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.workout)

        startButton = findViewById(R.id.startButton)
        pauseButton = findViewById(R.id.pauseButton)
        resetButton = findViewById(R.id.resetButton)
        timerDisplay = findViewById(R.id.timerDisplay)
        circularProgressIndicator = findViewById(R.id.circularProgressIndicator)

        startButton.setOnClickListener {
            startTimer()
        }

        pauseButton.setOnClickListener {
            pauseTimer()
        }

        resetButton.setOnClickListener {
            resetTimer()
        }

        circularProgressIndicator.max = 100
        circularProgressIndicator.progress = 100


        updateTimerText()
        updateProgressIndicator()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInSeconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInSeconds = millisUntilFinished / 1000
                updateTimerText()
                updateProgressIndicator()
            }

            override fun onFinish() {
                timerDisplay.text = "00:00"
                circularProgressIndicator.progress = 0
            }
        }.start()
    }

    private fun pauseTimer() {
        timer?.cancel()
    }

    private fun resetTimer() {
        timer?.cancel()
        timeLeftInSeconds = maxTimeInSeconds
        updateTimerText()
        updateProgressIndicator()
    }

    private fun updateTimerText() {
        val minutes = (timeLeftInSeconds / 60)
        val seconds = (timeLeftInSeconds % 60)
        val timeFormatted = String.format("%02d:%02d", minutes, seconds)
        timerDisplay.text = timeFormatted
    }

    private fun updateProgressIndicator() {
        val progress = (timeLeftInSeconds.toDouble() / maxTimeInSeconds * 100).toInt()
        circularProgressIndicator.progress = progress
    }
}
