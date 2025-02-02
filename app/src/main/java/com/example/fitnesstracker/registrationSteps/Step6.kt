package com.example.fitnesstracker.registrationSteps

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnesstracker.R
import com.example.fitnesstracker.database.DatabaseHelper
import com.example.fitnesstracker.utils.NavigationHelper

class Step6 : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.step6)

        dbHelper = DatabaseHelper(this)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val continueButton = findViewById<Button>(R.id.continueButton)

        continueButton.setOnClickListener {
            val selectedOptionId = radioGroup.checkedRadioButtonId

            if (selectedOptionId != -1) {
                val selectedOption = findViewById<RadioButton>(selectedOptionId).text.toString()
                val data = ContentValues().apply {
                    put("dietPlan", selectedOption)
                    put("stepCompleted", 6)  // Track progress
                }

                val isSaved = dbHelper.saveOrUpdateStepData(data, "user1")
                if (isSaved) {
                    NavigationHelper.navigateToNextStep(this, Step7::class.java)
                } else {
                    Toast.makeText(this, "Error saving data.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please select a diet option", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
