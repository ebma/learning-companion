package de.htwberlin.learningcompanion.places.details


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.AppDatabase
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.model.Place
import de.htwberlin.learningcompanion.places.overview.PlaceOverviewFragment
import de.htwberlin.learningcompanion.util.CapturedImageHelper
import de.htwberlin.learningcompanion.util.setActivityTitle
import kotlinx.android.synthetic.main.fragment_my_place.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MyPlaceFragment : Fragment() {

    private var RC_LOCATION_ACTIVITY = 9000
    private val RC_PICK_IMAGE = 9001
    private val REQUEST_TAKE_PHOTO = 9002


    private lateinit var rootView: View

    private lateinit var ivImagePreview: ImageView

    private lateinit var etName: EditText
    private lateinit var tvAddress: TextView

    private lateinit var btnSave: Button
    private lateinit var btnSetAddress: Button
    private lateinit var btnGetImageFromGallery: ImageButton
    private lateinit var btnTakeImage: ImageButton

    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    private var imageUri: Uri? = null

    private var mCurrentPhotoPath: String? = null


    private var editMode = false
    private var place: Place? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_my_place, container, false)

        findViews()
        addAddressClickListener()
        addGalleryButtonClickListener()
        addSaveButtonClickListener()
        addTakePictureClickListener()

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
        btnTakeImage = rootView.findViewById(R.id.ib_camera)
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

    private fun addTakePictureClickListener() {
        btnTakeImage.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            context!!,
                            "de.htwberlin.learningcompanion.fileprovider",
                            it
                    )
                    imageUri = photoURI
                    Log.d("MyPlaceFragment", "Photo path: " + imageUri)

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
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
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            if (imageUri != null) {

                if (mCurrentPhotoPath != null) {
                    val bitmap = CapturedImageHelper.handleSamplingAndRotationBitmap(context!!, imageUri!!)
                    bitmap?.let {
                        try {
                            CapturedImageHelper.saveBitmap(it, mCurrentPhotoPath!!)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
                Picasso.get().load(imageUri).fit().into(ivImagePreview)
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}
