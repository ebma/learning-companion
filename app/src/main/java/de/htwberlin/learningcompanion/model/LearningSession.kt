package de.htwberlin.learningcompanion.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import de.htwberlin.learningcompanion.learning.LightLevel
import de.htwberlin.learningcompanion.learning.NoiseLevel
import java.util.*

@Entity(tableName = "sessions", foreignKeys = [
    (ForeignKey(entity = Goal::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("goal_id"),
            onDelete = CASCADE)),
    (ForeignKey(entity = Place::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("place_id"),
            onDelete = CASCADE))])
data class LearningSession(
        @ColumnInfo(name = "place_id")
        var placeID: Long = 0,
        @ColumnInfo(name = "goal_id")
        var goalID: Long = 0) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var createdAt = Date()

    var userRating: Int = 0 // range 0..100

    var noiseValues = arrayListOf<Float>()
    var lightValues = arrayListOf<Float>()

    var noiseRating = NoiseLevel.MEDIUM
    var brightnessRating = LightLevel.MEDIUM
}
