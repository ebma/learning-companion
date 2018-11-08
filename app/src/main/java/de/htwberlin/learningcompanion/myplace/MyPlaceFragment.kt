package de.htwberlin.learningcompanion.myplace


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.MainActivity
import de.htwberlin.learningcompanion.R
import kotlinx.android.synthetic.main.fragment_my_place.*


class MyPlaceFragment : Fragment() {

    private var RC_LOCATION_ACTIVITY = 9000

    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).supportActionBar?.title = "My place"

        rootView = inflater.inflate(R.layout.fragment_my_place, container, false)

        addAddressClickListener()
        return rootView
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
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

}
