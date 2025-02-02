package com.example.fitnesstracker.database

import android.content.Context
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DataManager(private val context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun getUserData(): UserData? {
        val userId = "user1"
        val db = dbHelper.readableDatabase

        val query = "SELECT * FROM ${DatabaseHelper.TABLE_USERS} WHERE ${DatabaseHelper.COLUMN_USER_ID} = ?"
        val cursor = db.rawQuery(query, arrayOf(userId))

        var userData: UserData? = null
        if (cursor.moveToFirst()) {
            userData = UserData(
                firstName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FIRST_NAME)),
                lastName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LAST_NAME)),
                dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE_OF_BIRTH)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL)),
                gender = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GENDER)),
                height = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HEIGHT)),
                reasonForWeightGain = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REASON_FOR_WEIGHT_GAIN)),
                dietPlan = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DIET_PLAN)),
                weight = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WEIGHT)),
                goal = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GOAL)),
                dailyWorkingTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DAILY_WORKING_TIME)),
                fitnessLevel = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FITNESS_LEVEL)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD)),
                stepCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STEP_COMPLETED))
            )
            Log.d("DataManager", "User data retrieved: $userData")
        } else {
            Log.d("DataManager", "No user data found for userId: $userId")
        }

        cursor.close()
        db.close()
        return userData
    }


    fun calculateMetrics(user: UserData): Map<String, Any> {
        val bmi = calculateBMI(user.weight, user.height)
        val waterIntake = calculateDailyWaterIntake(user.weight)
        val age = calculateAge(user.dateOfBirth)
        val caloricNeeds = calculateCaloricNeeds(user.weight, user.height, age, user.gender, user.fitnessLevel)
        val nutrientNeeds = calculateNutrientNeeds(user.weight)
        val sleepNeed = calculateSleepNeed()

        return mapOf(
            "BMI" to bmi,
            "Daily Water Intake" to waterIntake,
            "Daily Caloric Needs" to caloricNeeds,
            "Nutrient Needs" to nutrientNeeds,
            "Sleep Need" to sleepNeed
        )
    }

    private fun calculateBMI(weight: Int, height: Int): Double {
        val heightInMeters = height / 100.0
        return weight / (heightInMeters * heightInMeters)
    }

    private fun calculateDailyWaterIntake(weight: Int): Int {
        return weight * 35
    }

    private fun calculateCaloricNeeds(weight: Int, height: Int, age: Int, gender: String, activityLevel: String): Int {
        val bmr = if (gender.lowercase() == "male") {
            88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
        } else {
            447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)
        }
        return (bmr * when(activityLevel.lowercase()) {
            "sedentary" -> 1.2
            "lightly active" -> 1.375
            "moderately active" -> 1.55
            "very active" -> 1.725
            "extra active" -> 1.9
            else -> 1.2
        }).toInt()
    }

    private fun calculateNutrientNeeds(weight: Int): Map<String, Int> {
        val protein = (0.8 * weight).toInt()
        val fat = (0.4 * weight).toInt()
        val carbs = (weight * 4) // Just a placeholder, actual calculation should adjust based on total calories
        return mapOf("protein" to protein, "fat" to fat, "carbs" to carbs)
    }

    private fun calculateSleepNeed(): Int {
        return 8 // assuming an average need of 8 hours
    }

    public fun calculateAge(dateOfBirth: String): Int {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dob = sdf.parse(dateOfBirth)
        val today = Calendar.getInstance()
        val birthDate = Calendar.getInstance().apply { time = dob }
        var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age
    }
}
