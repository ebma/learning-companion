package de.htwberlin.learningcompanion.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class Place(
        val imageUri: String,
        val name: String,
        val latitude: Double,
        val longitude: Double,
        val addressString: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var currentPlace: Boolean = false
}
