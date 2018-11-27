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
import com.warkiz.widget.IndicatorSeekBar
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.db.LearningSessionRepository
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.mainscreen.MainScreenFragment
import de.htwberlin.learningcompanion.model.Goal
import de.htwberlin.learningcompanion.model.LearningSession
import de.htwberlin.learningcompanion.model.Place

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

    private lateinit var currentGoal: Goal
    private lateinit var currentPlace: Place
    private lateinit var currentLearningSession: LearningSession

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        placeTextView = rootView.findViewById(R.id.tv_place_text)
        ivPlaceBackground = rootView.findViewById(R.id.iv_place_background)
        btnNext = rootView.findViewById(R.id.btn_next)
        sbNoiseRating = rootView.findViewById(R.id.sb_noise_rating)
        sbBrightnessRating = rootView.findViewById(R.id.sb_brightness_rating)

        currentGoal = GoalRepository.get(context!!).getCurrentGoal()!!
        currentPlace = PlaceRepository.get(context!!).getCurrentPlace()!!
        currentLearningSession = LearningSessionRepository.get(context!!).getLearningSessionByGoalAndPlaceID(currentGoal.id, currentPlace.id)

        setBackgroundPicture()
        setPlaceText()
        addButtonClickListener()

        if (permissionsGranted()) {
            // means that we collected the values and the user should not choose them
            sbNoiseRating.isEnabled = false
            sbNoiseRating.setProgress(currentLearningSession.noiseRating.ordinal.toFloat())
            sbBrightnessRating.isEnabled = false
            sbBrightnessRating.setProgress(currentLearningSession.noiseRating.ordinal.toFloat())
        }
    }

    private fun permissionsGranted(): Boolean {
        // TODO implement with actual permission check
        return true
    }

    private fun addButtonClickListener() {
        btnNext.setOnClickListener {
            navigateToMainScreen()
        }
    }

    private fun navigateToMainScreen() {
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
