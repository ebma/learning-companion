package de.htwberlin.learningcompanion.myplace

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import de.htwberlin.learningcompanion.R
import kotlinx.android.synthetic.main.activity_get_location.*
import kotlinx.android.synthetic.main.content_get_location.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


/**
 * An activity that displays a map showing the place at the device's current location.
 */
class GetLocationActivity : AppCompatActivity() {
    private var map: MapView? = null

    private val defaultLocation = GeoPoint(52.504043, 13.393236)
    private var locationPermissionGranted: Boolean = false

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null


    companion object {
        private val TAG = GetLocationActivity::class.java.simpleName
        private val DEFAULT_ZOOM = 15.0
        private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

    private lateinit var mLocationOverlay: MyLocationNewOverlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_get_location)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Get location of current place"

        Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(ctx))

        if (!locationPermissionGranted) {
            getLocationPermission()
        }

        map = findViewById(R.id.map)
        map?.setTileSource(TileSourceFactory.MAPNIK)

        map?.setBuiltInZoomControls(true)
        map?.setMultiTouchControls(true)

        map?.controller?.setZoom(DEFAULT_ZOOM)
        moveCameraToLocation(defaultLocation)

        addLocationOverlay()

        getDeviceLocation()

        btn_save_location.onClick {
            toast("Location saved!")
        }

    }

    public override fun onResume() {
        super.onResume()
        map?.onResume()
        mLocationOverlay.enableMyLocation()
    }

    public override fun onPause() {
        super.onPause()
        map?.onPause()
    }

    private fun addLocationOverlay() {
        mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(applicationContext), map)
        mLocationOverlay.enableMyLocation()
        map?.overlays?.add(mLocationOverlay)
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private fun getDeviceLocation() {
        if (locationPermissionGranted) {
            val gpsMyLocationProvider = GpsMyLocationProvider(applicationContext)
            val location = gpsMyLocationProvider.lastKnownLocation
            Log.d(TAG, "last known location $location")

            gpsMyLocationProvider.startLocationProvider { location, source ->
                moveCameraToLocation(location)
                Log.d(TAG, "location changed to ${location?.latitude} ${location?.longitude}")
            }
        }
    }

    private fun moveCameraToLocation(location: Location?) {
        if (location != null) {
            val geoPoint = GeoPoint(location)
            map?.controller?.setCenter(geoPoint)
        }
    }

    private fun moveCameraToLocation(geoPoint: GeoPoint) {
        map?.controller?.setCenter(geoPoint)
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private fun getLocationPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)

            }
        } else {
            locationPermissionGranted = true
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                    getDeviceLocation()
                }
            }
        }
    }
}