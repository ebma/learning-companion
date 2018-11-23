package de.htwberlin.learningcompanion.learning.evaluation


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.warkiz.widget.IndicatorSeekBar
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.mainscreen.MainScreenFragment

class EvaluatePlaceFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var ivPlaceBackground: ImageView
    private lateinit var placeTextView: TextView
    private lateinit var btnNext: ImageButton
    private lateinit var sbNoiseRating: IndicatorSeekBar
    private lateinit var sbBrightnessRating: IndicatorSeekBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_evaluate_place, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        placeTextView = rootView.findViewById(R.id.tv_place_text)
        ivPlaceBackground = rootView.findViewById(R.id.iv_place_background)
        btnNext = rootView.findViewById(R.id.btn_next)
        sbNoiseRating = rootView.findViewById(R.id.sb_noise_rating)
        sbBrightnessRating = rootView.findViewById(R.id.sb_brightness_rating)

        setBackgroundPicture()
        setPlaceText()
        addButtonClickListener()
    }

    private fun addButtonClickListener() {
        btnNext.setOnClickListener {
            navigateToMainScreen()
        }
    }

    // noch Funktionen -> save Rating to Database...

    private fun navigateToMainScreen() {
//        val bundle = Bundle()
//        bundle.putInt("noise_rating", sbNoiseRating.progress)
//        bundle.putInt("brightness_rating", sbBrightnessRating.progress)
//        Navigation.findNavController(rootView).navigate(R.id.action_evaluatePlaceAchieved_to_mainScreenFragment, bundle)

        val fragment = MainScreenFragment()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.content_main, fragment)?.commit()
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
