package de.htwberlin.learningcompanion.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class Place(
        val background: String,
        val name: String,
        val address: String,
        val addressString: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
