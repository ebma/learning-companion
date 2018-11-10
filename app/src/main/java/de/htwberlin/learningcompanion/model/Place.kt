package de.htwberlin.learningcompanion.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class Place(
        val imageUri: Uri,
        val name: String,
        val latitude: Double,
        val longitude: Double,
        val addressString: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var currentPlace: Boolean = false
}
