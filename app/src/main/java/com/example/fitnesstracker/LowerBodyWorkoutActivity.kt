package com.example.fitnesstracker

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LowerBodyWorkoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.lowerbodyworkout_activity)
    }

    // Click handler for card views
    fun onCardClick(view: View) {
        val scaleUp = AnimatorInflater.loadAnimator(this, R.animator.scale_up) as AnimatorSet
        val scaleDown = AnimatorInflater.loadAnimator(this, R.animator.scale_down) as AnimatorSet
        scaleUp.setTarget(view)
        scaleDown.setTarget(view)

        scaleUp.start()
        scaleUp.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                scaleDown.start()
            }
        })

        Toast.makeText(this, "Card clicked!", Toast.LENGTH_SHORT).show()
    }

    // Click handler for the "Start Workout" button
    fun onStartWorkoutClick(view: View) {
        val fadeIn = AnimatorInflater.loadAnimator(this, R.animator.fade_in) as AnimatorSet
        fadeIn.setTarget(view)
        fadeIn.start()

        // Additional logic for starting the workout can be added here
        Toast.makeText(this, "Workout Started!", Toast.LENGTH_SHORT).show()
    }
}
