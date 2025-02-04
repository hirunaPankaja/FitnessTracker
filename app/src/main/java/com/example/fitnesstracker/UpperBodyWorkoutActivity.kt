package com.example.fitnesstracker

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UpperBodyWorkoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upperbodyworkout_activity)
    }

    fun onCardClick(view: View) {
        Log.d("UpperBodyWorkoutActivity", "Card clicked: ${view.id}")

        val exerciseName = when (view.id) {
            R.id.imgPushUps -> "PushUps"
            R.id.imgDips -> "Dips"
            R.id.imgPlankToPushUp -> "PlankToPushUp"
            else -> {
                Log.e("UpperBodyWorkoutActivity", "Unknown view ID: ${view.id}")
                return
            }
        }

        Log.d("UpperBodyWorkoutActivity", "Exercise: $exerciseName")
        val intent = Intent(this, Workout::class.java)
        intent.putExtra("exerciseName", exerciseName)
        startActivity(intent)
    }

    fun onStartWorkoutClick(view: View) {
        val fadeIn = AnimatorInflater.loadAnimator(this, R.animator.fade_in) as AnimatorSet
        fadeIn.setTarget(view)
        fadeIn.start()

        Toast.makeText(this, "Workout Started!", Toast.LENGTH_SHORT).show()
    }
}
