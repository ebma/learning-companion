package de.htwberlin.learningcompanion.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.htwberlin.learningcompanion.model.Goal
import de.htwberlin.learningcompanion.model.Place

@Database(entities = [Goal::class, Place::class], version = 1)
@TypeConverters(UriTypeConverter::class, DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun goalDao(): GoalDAO

    abstract fun placeDao(): PlaceDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "database.db").allowMainThreadQueries().build()
    }
}