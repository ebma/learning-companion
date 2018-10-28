package de.htwberlin.learningcompanion.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goal")
data class Goal(
        val action: String,
        val amount: String,
        val field: String,
        val medium: String,
        val durationInMin: Int? = null,
        val untilTimeStamp: String? = null) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
