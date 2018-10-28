package de.htwberlin.learningcompanion.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goal")
data class Goal(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val amount: String,
        val action: String,
        val field: String,
        val medium: String,
        val durationInMin: Int,
        val untilTimeStamp: String)