package com.example.fitnesstracker

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class step10 : AppCompatActivity() {
    private lateinit var spinnerYear: Spinner
    private lateinit var spinnerMonth: Spinner
    private lateinit var spinnerDay: Spinner
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.step10)

        spinnerYear = findViewById(R.id.spinnerYear)
        spinnerMonth = findViewById(R.id.spinnerMonth)
        spinnerDay = findViewById(R.id.spinnerDay)
        tvResult = findViewById(R.id.tvResult)

        // Initialize Spinners
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (1900..currentYear).toList().reversed()
        val months = (1..12).toList()
        val days = (1..31).toList()

        spinnerYear.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, years)
        spinnerMonth.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, months)
        spinnerDay.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days)

        // Add listeners
        spinnerYear.onItemSelectedListener = createItemSelectedListener()
        spinnerMonth.onItemSelectedListener = createItemSelectedListener()
        spinnerDay.onItemSelectedListener = createItemSelectedListener()
    }

    private fun createItemSelectedListener() = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            calculateAge()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // Do nothing
        }
    }

    private fun calculateAge() {
        val year = spinnerYear.selectedItem as? Int
        val month = spinnerMonth.selectedItem as? Int
        val day = spinnerDay.selectedItem as? Int

        if (year != null && month != null && day != null) {
            val today = Calendar.getInstance()
            val birthDate = Calendar.getInstance().apply {
                set(year, month - 1, day)
            }

            if (birthDate.after(today)) {
                tvResult.text = "Invalid date! Birth date cannot be in the future."
                return
            }

            val age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
                tvResult.text = "Your age is: ${age - 1} years old"
            } else {
                tvResult.text = "Your age is: $age years old"
            }
        } else {
            tvResult.text = "Please select a valid date."
        }
    }
}
