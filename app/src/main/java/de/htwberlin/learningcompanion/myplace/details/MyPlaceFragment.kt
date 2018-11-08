package de.htwberlin.learningcompanion.myplace.details


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import de.htwberlin.learningcompanion.MainActivity
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.AppDatabase
import de.htwberlin.learningcompanion.mainscreen.MainScreenFragment
import de.htwberlin.learningcompanion.model.Place
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_my_place.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast


class MyPlaceFragment : Fragment() {

    private var RC_LOCATION_ACTIVITY = 9000
    private val RC_PICK_IMAGE = 9001

    private lateinit var rootView: View

    private lateinit var etName: EditText
    private lateinit var etAddress: EditText
    private lateinit var btnSave: Button

    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    private var imageUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).supportActionBar?.title = "My place"

        rootView = inflater.inflate(R.layout.fragment_my_place, container, false)

        findViews()
        addAddressClickListener()
        addGalleryButtonClickListener()
        addSaveButtonClickListener()

        return rootView
    }

    private fun findViews() {
        etName = rootView.findViewById(R.id.et_name)
        etAddress = rootView.findViewById(R.id.et_address)
        btnSave = rootView.findViewById(R.id.btn_save)
    }

    private fun addSaveButtonClickListener() {
        btnSave.onClick {
            val nameString = etName.text.toString()
            val addressString = etAddress.text.toString()

            if (nameString.isNotEmpty() && addressString.isNotEmpty() && imageUri != null) {
                val place = Place(imageUri.toString(), nameString, latitude, longitude, addressString)
                savePlace(place)
                toast("Place saved to Database")
                navigateToMainScreen()
            }
        }
    }

    private fun savePlace(place: Place) {
        context?.let { AppDatabase.get(it).placeDao().insertPlace(place) }
    }

    private fun navigateToMainScreen() {
        (activity as MainActivity).nav_view.setCheckedItem(R.id.nav_mainscreen)

        val fragment = MainScreenFragment()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.content_main, fragment)?.commit()
    }

    private fun addGalleryButtonClickListener() {
        rootView.findViewById<ImageButton>(R.id.ib_gallery).onClick {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), RC_PICK_IMAGE)
        }
    }

    private fun addAddressClickListener() {
        rootView.findViewById<EditText>(R.id.et_address).setOnClickListener {
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
                    val address = data?.extras?.getString(LOCATION_STRING_EXTRA)
                    et_address.setText(address)
                }
                Activity.RESULT_CANCELED -> {

                }
            }
        } else if (requestCode == RC_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            imageUri = uri

            Picasso.get().load(uri).fit().into(iv_image_preview)
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

}
