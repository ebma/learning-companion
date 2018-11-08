package de.htwberlin.learningcompanion.db

import androidx.lifecycle.LiveData
import androidx.room.*
import de.htwberlin.learningcompanion.model.Place

@Dao
interface PlaceDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlace(place: Place)

    @Update
    fun updatePlace(place: Place)

    @Delete
    fun deletePlace(place: Place)

    @Query("SELECT * FROM places WHERE id == :id")
    fun getPlaceByID(id: Int): List<Place>

    @Query("SELECT * FROM places")
    fun getPlaces(): List<Place>

    @Query("SELECT * FROM places")
    fun getPlacesAsLiveData(): LiveData<List<Place>>
}