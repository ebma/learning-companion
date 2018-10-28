package de.htwberlin.learningcompanion.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.htwberlin.learningcompanion.model.Goal

@Database(entities = [Goal::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun goalDao(): GoalDAO

    companion object {
        var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "database.db").allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }
    }
}