package com.example.fitnesstracker.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "fitness_tracker.db"
        const val DATABASE_VERSION = 2

        // Table name and columns
        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "userId"
        const val COLUMN_FIRST_NAME = "firstName"
        const val COLUMN_LAST_NAME = "lastName"
        const val COLUMN_DATE_OF_BIRTH = "dateOfBirth"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_GENDER = "gender"
        const val COLUMN_HEIGHT = "height"
        const val COLUMN_REASON_FOR_WEIGHT_GAIN = "reasonForWeightGain"
        const val COLUMN_DIET_PLAN = "dietPlan"
        const val COLUMN_WEIGHT = "weight"
        const val COLUMN_GOAL = "goal"
        const val COLUMN_DAILY_WORKING_TIME = "dailyWorkingTime"
        const val COLUMN_FITNESS_LEVEL = "fitnessLevel"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_STEP_COMPLETED = "stepCompleted"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID TEXT PRIMARY KEY,
                $COLUMN_FIRST_NAME TEXT,
                $COLUMN_LAST_NAME TEXT,
                $COLUMN_DATE_OF_BIRTH TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_GENDER TEXT,
                $COLUMN_HEIGHT INTEGER,
                $COLUMN_REASON_FOR_WEIGHT_GAIN TEXT,
                $COLUMN_DIET_PLAN TEXT,
                $COLUMN_WEIGHT INTEGER,
                $COLUMN_GOAL TEXT,
                $COLUMN_DAILY_WORKING_TIME TEXT,
                $COLUMN_FITNESS_LEVEL TEXT,
                $COLUMN_PASSWORD TEXT,
                $COLUMN_STEP_COMPLETED INTEGER
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_USERS ADD COLUMN $COLUMN_EMAIL TEXT")
        }
    }

    /**
     * Save or update data step by step using userId as the unique identifier
     */
    fun saveOrUpdateStepData(data: ContentValues, userId: String): Boolean {
        val db = writableDatabase

        // Check if the user data already exists
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_USER_ID),
            "$COLUMN_USER_ID = ?",
            arrayOf(userId),
            null, null, null
        )

        val rowExists = cursor.moveToFirst()
        cursor.close()

        data.put(COLUMN_USER_ID, userId) // Ensure userId is included in the data

        return if (rowExists) {
            // If data exists, update the existing row
            db.update(TABLE_USERS, data, "$COLUMN_USER_ID = ?", arrayOf(userId)) > 0
        } else {
            // If data does not exist, insert new data
            db.insert(TABLE_USERS, null, data) != -1L
        }.also {
            db.close()
        }
    }

    /**
     * Get all user data
     */
    fun getAllUserData(): List<UserData> {
        val db = readableDatabase
        val userDataList = mutableListOf<UserData>()

        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS", null)

        if (cursor.moveToFirst()) {
            do {
                val userData = UserData(
                    firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                    lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                    dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_OF_BIRTH)),
                    email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    gender = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER)),
                    height = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HEIGHT)),
                    reasonForWeightGain = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REASON_FOR_WEIGHT_GAIN)),
                    dietPlan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIET_PLAN)),
                    weight = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)),
                    goal = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GOAL)),
                    dailyWorkingTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAILY_WORKING_TIME)),
                    fitnessLevel = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FITNESS_LEVEL)),
                    password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                    stepCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STEP_COMPLETED))
                )
                userDataList.add(userData)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return userDataList
    }
}
