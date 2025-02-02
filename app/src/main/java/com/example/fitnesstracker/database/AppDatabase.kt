package com.example.fitnesstracker.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Route::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routeDao(): RouteDao
}
