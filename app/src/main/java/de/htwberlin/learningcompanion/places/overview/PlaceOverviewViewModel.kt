package de.htwberlin.learningcompanion.places.overview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import de.htwberlin.learningcompanion.db.AppDatabase
import de.htwberlin.learningcompanion.model.Place

class PlaceOverviewViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var places: LiveData<List<Place>>

    fun getPlaces(): LiveData<List<Place>> {
        if (!::places.isInitialized) {
            loadPlaces()
        }
        return places
    }

    private fun loadPlaces() {
        places = AppDatabase.get(getApplication()).placeDao().getPlacesAsLiveData()
    }
}
