package com.example.fitnesstracker

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class Step3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.step3)

        // Find necessary views from the layout
        val heightPicker = findViewById<NumberPicker>(R.id.heightPicker)
        val tvConvertedHeight = findViewById<TextView>(R.id.convertedHeight)
        val cmSelector = findViewById<RadioButton>(R.id.cmSelector)
        val feetSelector = findViewById<RadioButton>(R.id.feetSelector)

        // Set the height picker range
        heightPicker.minValue = 100 // Min height in cm
        heightPicker.maxValue = 250 // Max height in cm
        heightPicker.value = 180 // Default height value

        // Set a listener for changes in the height picker
        heightPicker.setOnValueChangedListener { _, _, newVal ->
            updateHeightDisplay(newVal, cmSelector.isChecked, tvConvertedHeight)
        }

        // Handle unit selection (CM or Feet)
        cmSelector.setOnCheckedChangeListener { _, isChecked ->
            updateHeightDisplay(heightPicker.value, isChecked, tvConvertedHeight)
        }

        feetSelector.setOnCheckedChangeListener { _, isChecked ->
            updateHeightDisplay(heightPicker.value, isChecked, tvConvertedHeight)
        }
    }

    private fun updateHeightDisplay(cmHeight: Int, isCm: Boolean, textView: TextView) {
        if (isCm) {
            // Show height in cm
            textView.text = "$cmHeight cm"
        } else {
            // Convert height to feet and inches
            val feet = cmHeight / 30.48
            val inches = (cmHeight % 30.48) / 2.54
            textView.text = String.format("%.0f feet %.0f inches", feet, inches)
        }
    }
}
