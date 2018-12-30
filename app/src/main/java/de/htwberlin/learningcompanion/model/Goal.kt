package de.htwberlin.learningcompanion.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.htwberlin.learningcompanion.learning.LightLevel
import de.htwberlin.learningcompanion.learning.NoiseLevel
import java.util.*

@Entity(tableName = "goals")
data class Goal(
        val action: String,
        val amount: String,
        val field: String,
        val medium: String,
        var durationInMin: Int? = null,
        var untilTimeStamp: String? = null) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var createdAt = Date()

    var completed: Boolean = false
    var currentGoal: Boolean = false

    var userRating: Int = 0 // range 0..100
    var noiseRating = NoiseLevel.MEDIUM;
    var brightnessRating = LightLevel.MEDIUM;

    fun getGoalText(): String {
        return when {
            untilTimeStamp != null -> "$action $amount $field $medium until $untilTimeStamp"
            durationInMin != null -> "$action $amount $field $medium for $durationInMin minutes"
            else -> "${action}, ${field}, ${medium}, ${amount}"
        }
    }
}
