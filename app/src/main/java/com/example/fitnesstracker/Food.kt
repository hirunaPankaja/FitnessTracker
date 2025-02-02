package com.example.fitnesstracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fitnesstracker.database.DataManager
import android.util.Log

class Food : Fragment() {

    private lateinit var dataManager: DataManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_food, container, false)

        dataManager = DataManager(requireContext())

        loadUserData(view) // Load the user data and display metrics

        return view
    }

    private fun loadUserData(view: View) {
        val userData = dataManager.getUserData()
        userData?.let { user ->
            val metrics = dataManager.calculateMetrics(user)
            displayMetrics(view, metrics)
        } ?: run {
            Log.d("Food", "No user data found")
        }
    }

    private fun displayMetrics(view: View, metrics: Map<String, Any>) {
        val bmi = metrics["BMI"] as Double
        val waterIntake = metrics["Daily Water Intake"] as Int
        val caloricNeeds = metrics["Daily Caloric Needs"] as Int
        val nutrientNeeds = metrics["Nutrient Needs"] as Map<String, Int>
        val sleepNeed = metrics["Sleep Need"] as Int

        view.findViewById<TextView>(R.id.bmiTextView).text = getString(R.string.bmi_text, bmi)
        view.findViewById<TextView>(R.id.waterIntakeTextView).text = getString(R.string.water_intake_text, waterIntake)
        view.findViewById<TextView>(R.id.caloricNeedsTextView).text = getString(R.string.caloric_needs_text, caloricNeeds)
        view.findViewById<TextView>(R.id.proteinTextView).text = getString(R.string.protein_text, nutrientNeeds["protein"])
        view.findViewById<TextView>(R.id.fatTextView).text = getString(R.string.fat_text, nutrientNeeds["fat"])
        view.findViewById<TextView>(R.id.carbsTextView).text = getString(R.string.carbs_text, nutrientNeeds["carbs"])
        view.findViewById<TextView>(R.id.sleepNeedTextView).text = getString(R.string.sleep_need_text, sleepNeed)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Food().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
