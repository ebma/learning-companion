package de.htwberlin.learningcompanion.learning.evaluation


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.buddy.BuddyFaceHolder
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.db.LearningSessionRepository
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.learning.session.SessionOverviewFragment
import de.htwberlin.learningcompanion.model.Goal
import de.htwberlin.learningcompanion.model.LearningSession
import de.htwberlin.learningcompanion.model.Place
import kotlinx.android.synthetic.main.fragment_evaluate_place.*

class EvaluatePlaceFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_evaluate_place, container, false)
        return rootView
    }

    private lateinit var currentGoal: Goal
    private lateinit var currentPlace: Place
    private lateinit var currentLearningSession: LearningSession

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        iv_charlie.setImageDrawable(BuddyFaceHolder.get(context!!).getDefaultFace())
        currentGoal = GoalRepository.get(context!!).getCurrentGoal()!!
        currentPlace = PlaceRepository.get(context!!).getCurrentPlace()!!
        currentLearningSession = LearningSessionRepository.get(context!!).getNewestLearningSession()

        setBackgroundPicture()
        setPlaceText()
        addButtonClickListener()

        if (permissionsGranted()) {
            // means that we collected the values and the user should not choose them
            sb_noise_rating.setProgress(currentLearningSession.noiseRating.ordinal.toFloat())
            sb_noise_rating.isEnabled = false
            sb_brightness_rating.setProgress(currentLearningSession.brightnessRating.ordinal.toFloat())
            sb_brightness_rating.isEnabled = false
        }
    }

    private fun permissionsGranted(): Boolean {
        // TODO implement with actual permission check
        return true
    }

    private fun addButtonClickListener() {
        btn_next.setOnClickListener {
            navigateToSessionOverview()
        }
    }

    private fun navigateToSessionOverview() {
        val fragment = SessionOverviewFragment()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.content_main, fragment)?.commit()
    }

    private fun setPlaceText() {
        val name = PlaceRepository.get(context!!).getCurrentPlace()?.name
        val address = PlaceRepository.get(context!!).getCurrentPlace()?.addressString
        if (address == null || address == "") {
            tv_place_text.text = "$name"
        } else {
            tv_place_text.text = "$name \n$address"
        }
    }

    private fun setBackgroundPicture() {
        val currentPlace = PlaceRepository.get(context!!).getCurrentPlace()

        if (currentPlace != null) {
            if (currentPlace.imageUri != null) {
                val inputStream = activity!!.contentResolver.openInputStream(currentPlace.imageUri)
                val drawable = Drawable.createFromStream(inputStream, currentPlace.imageUri.toString())
                iv_place_background.setImageDrawable(drawable)
            }
        }
    }


}
