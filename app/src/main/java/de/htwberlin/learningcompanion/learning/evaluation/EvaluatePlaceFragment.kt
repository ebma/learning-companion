package de.htwberlin.learningcompanion.learning.evaluation


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.PlaceRepository


class EvaluatePlaceFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var placeTextView: TextView
    private lateinit var ivPlaceBackground: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_evaluate_place, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        placeTextView = rootView.findViewById(R.id.tv_place_text)
        ivPlaceBackground = rootView.findViewById(R.id.iv_place_background)
        setBackgroundPicture()
        setPlaceText()
    }

    private fun setPlaceText() {
        val name = PlaceRepository.get(context!!).getCurrentPlace()?.name
        val address = PlaceRepository.get(context!!).getCurrentPlace()?.addressString
        if (address == null || address == "") {
            placeTextView.text = "$name"
        } else {
            placeTextView.text = "$name \n$address"
        }
    }

    private fun setBackgroundPicture() {
        val currentPlace = PlaceRepository.get(context!!).getCurrentPlace()

        if (currentPlace != null) {
            if (currentPlace.imageUri != null) {
                val inputStream = activity!!.contentResolver.openInputStream(currentPlace.imageUri)
                val drawable = Drawable.createFromStream(inputStream, currentPlace.imageUri.toString())
                ivPlaceBackground.setImageDrawable(drawable)
            }
        }
    }


}
