package de.htwberlin.learningcompanion.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import de.htwberlin.learningcompanion.model.Place
import java.util.concurrent.Executors

class PlaceRepository private constructor(context: Context) {

    private val context: Context = context.applicationContext

    var placesLiveData: LiveData<List<Place>>

    private val appDatabase: AppDatabase
    private val executor = Executors.newSingleThreadExecutor()

    private val allPlaces: LiveData<List<Place>>
        get() = appDatabase.placeDao().getPlacesAsLiveData()

    init {
        appDatabase = AppDatabase.get(this.context)
        placesLiveData = allPlaces
    }

    fun setPlaceAsCurrentPlace(place: Place) {
        val places = appDatabase.placeDao().getPlaces()

        places.forEach {
            it.currentPlace = it.id == place.id
        }

        appDatabase.placeDao().updatePlaces(places)
    }

    fun getPlaceByID(placeID: Long): Place {
        return appDatabase.placeDao().getPlaceByID(placeID)
    }

    fun insertPlaceList(placeList: List<Place>) {
        executor.execute { appDatabase.placeDao().insertPlaces(placeList) }
    }

    fun insertPlace(place: Place) {
        executor.execute { appDatabase.placeDao().insertPlace(place) }
    }

    fun deletePlace(place: Place) {
        executor.execute { appDatabase.placeDao().deletePlace(place) }
    }

    companion object {
        private val TAG = PlaceRepository::class.java.simpleName

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: PlaceRepository? = null

        fun get(context: Context): PlaceRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: PlaceRepository(context).also { INSTANCE = it }
        }
    }
}
