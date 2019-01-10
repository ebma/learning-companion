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
import de.htwberlin.learningcompanion.buddy.BuddyFaceHolder
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.db.LearningSessionRepository
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.model.Goal
import de.htwberlin.learningcompanion.model.LearningSession
import de.htwberlin.learningcompanion.model.Place
import kotlinx.android.synthetic.main.fragment_evaluate_goal_achieved.*

class EvaluateGoalAchieved : Fragment() {

    private lateinit var rootView: View
    private lateinit var ivPlaceBackground: ImageView
    private lateinit var tvGoalTextView: TextView
    private lateinit var btnNext: ImageButton
    private lateinit var sbUserRating: IndicatorSeekBar

    private lateinit var currentPlace: Place
    private lateinit var currentGoal: Goal
    private lateinit var currentLearningSession: LearningSession

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_evaluate_goal_achieved, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        iv_charlie.setImageDrawable(BuddyFaceHolder.get(context!!).getDefaultFace())
        ivPlaceBackground = rootView.findViewById(R.id.iv_place_background)
        tvGoalTextView = rootView.findViewById(R.id.tv_goal_text)
        btnNext = rootView.findViewById(R.id.btn_next)
        sbUserRating = rootView.findViewById(R.id.sb_user_rating)

        currentGoal = GoalRepository.get(context!!).getCurrentGoal()!!
        currentPlace = PlaceRepository.get(context!!).getCurrentPlace()!!
        currentLearningSession = LearningSessionRepository.get(context!!).getNewestLearningSession()

        setBackgroundPicture()
        setGoalText()
        addButtonClickListener()

    }

    private fun addButtonClickListener() {
        btnNext.setOnClickListener {
            updateSessionWithUserRating()
            navigateToNextFragmentWithValues()
        }

    }

    private fun updateSessionWithUserRating() {
        currentLearningSession.userRating = sbUserRating.progress
        LearningSessionRepository.get(context!!).updateLearningSession(currentLearningSession)
    }

    private fun navigateToNextFragmentWithValues() {
        Navigation.findNavController(rootView).navigate(R.id.action_evaluateGoalAchieved_to_evaluatePlaceFragment, null)
    }

    private fun setGoalText() {
        tvGoalTextView.text = GoalRepository.get(context!!).getCurrentGoal()?.getGoalText()
    }

    private fun setBackgroundPicture() {
        if (currentPlace.imageUri != null) {
            val inputStream = activity!!.contentResolver.openInputStream(currentPlace.imageUri!!)
            val drawable = Drawable.createFromStream(inputStream, currentPlace.imageUri.toString())
            ivPlaceBackground.setImageDrawable(drawable)
        }
    }
}
