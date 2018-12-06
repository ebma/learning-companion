package de.htwberlin.learningcompanion.places.details


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.AppDatabase
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.model.Place
import de.htwberlin.learningcompanion.places.overview.PlaceOverviewFragment
import de.htwberlin.learningcompanion.util.setActivityTitle
import kotlinx.android.synthetic.main.fragment_my_place.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast


class MyPlaceFragment : Fragment() {

    private var RC_LOCATION_ACTIVITY = 9000
    private val RC_PICK_IMAGE = 9001

    private lateinit var rootView: View

    private lateinit var ivImagePreview: ImageView

    private lateinit var etName: EditText
    private lateinit var tvAddress: TextView

    private lateinit var btnSave: Button
    private lateinit var btnSetAddress: Button
    private lateinit var btnGetImageFromGallery: ImageButton

    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    private var imageUri: Uri? = null

    private var editMode = false
    private var place: Place? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_my_place, container, false)

        findViews()
        addAddressClickListener()
        addGalleryButtonClickListener()
        addSaveButtonClickListener()

        checkForEditablePlace()

        return rootView
    }

    private fun checkForEditablePlace() {
        if (arguments != null) {
            val id = arguments!!.getLong("ID")
            context?.let {
                place = AppDatabase.get(it).placeDao().getPlaceByID(id)
            }
            if (place != null) {
                initLayoutWithPlace(place!!)
            }

            setActivityTitle("Edit place")
            editMode = true

        } else {
            setActivityTitle("New Place")
            editMode = false
        }
    }

    private fun initLayoutWithPlace(place: Place) {
        etName.setText(place.name)
        tvAddress.text = place.addressString
        imageUri = place.imageUri

        Picasso.get().load(imageUri).fit().into(ivImagePreview)
    }

    private fun findViews() {
        etName = rootView.findViewById(R.id.et_name)
        tvAddress = rootView.findViewById(R.id.tv_address)
        ivImagePreview = rootView.findViewById(R.id.iv_image_preview)
        btnSave = rootView.findViewById(R.id.btn_save)
        btnSetAddress = rootView.findViewById(R.id.btn_set_address)
        btnGetImageFromGallery = rootView.findViewById(R.id.ib_gallery)
    }

    private fun addSaveButtonClickListener() {
        btnSave.onClick {
            val nameString = etName.text.toString()
            val addressString = tvAddress.text.toString()

            if (nameString.isNotEmpty()) {
                if (editMode && place != null) {
                    val updatedPlace = Place(imageUri, nameString, latitude, longitude, addressString)
                    updatedPlace.id = place?.id ?: 0
                    updatePlace(updatedPlace)
                    toast("Place updated")
                } else {
                    val place = Place(imageUri, nameString, latitude, longitude, addressString)
                    savePlace(place)
                    toast("Place saved to Database")
                }
                navigateToPlaceOverview()
            } else {
                toast("Missing name")
            }
        }
    }

    private fun updatePlace(place: Place) {
        context?.let { PlaceRepository.get(it).updatePlace(place) }

    }

    private fun savePlace(place: Place) {
        context?.let {
            place.currentPlace = true
            PlaceRepository.get(it).setNoPlaceAsCurrent()
            PlaceRepository.get(it).insertPlace(place)
        }
    }

    private fun navigateToPlaceOverview() {
        // (activity as MainActivity).nav_view.setCheckedItem(R.id.nav_mainscreen)

        val fragment = PlaceOverviewFragment()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.content_main, fragment)?.commit()
    }

    private fun addGalleryButtonClickListener() {
        btnGetImageFromGallery.onClick {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_OPEN_DOCUMENT // this is freaking important, else the uri will not be persistable
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), RC_PICK_IMAGE)
        }
    }

    private fun addAddressClickListener() {
        btnSetAddress.setOnClickListener {
            startGetLocationActivity()
        }
    }

    private fun startGetLocationActivity() {
        val intent = Intent(context, GetLocationActivity::class.java)
        startActivityForResult(intent, RC_LOCATION_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_LOCATION_ACTIVITY) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val addressDisplayName = data?.extras?.getString(LOCATION_DISPLAYNAME_EXTRA)
                    tv_address.text = addressDisplayName

                    latitude = data?.extras?.getDouble(LOCATION_LATITUDE_EXTRA) ?: 0.0
                    longitude = data?.extras?.getDouble(LOCATION_LONGITUDE_EXTRA) ?: 0.0

                }
                Activity.RESULT_CANCELED -> {

                }
            }
        } else if (requestCode == RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            imageUri = uri

            Picasso.get().load(uri).fit().into(ivImagePreview)
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
