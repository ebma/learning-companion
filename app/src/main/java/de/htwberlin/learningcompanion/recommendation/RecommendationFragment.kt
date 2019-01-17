package de.htwberlin.learningcompanion.recommendation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.buddy.BuddyFaceHolder
import de.htwberlin.learningcompanion.learning.LightLevel
import de.htwberlin.learningcompanion.learning.NoiseLevel
import de.htwberlin.learningcompanion.util.setActivityTitle
import kotlinx.android.synthetic.main.fragment_recommendation.*
import org.jetbrains.anko.support.v4.runOnUiThread

class RecommendationFragment : Fragment() {

    private lateinit var recommendationHelper: RecommendationHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recommendation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setActivityTitle(getString(R.string.title_nav_menu_recommendation))

        recommendationHelper = RecommendationHelper(context!!)

        iv_charlie.setImageDrawable(BuddyFaceHolder.get(context!!).getDefaultFace())

        recommendationHelper.calculateRecommendation(object : RecommendationHelper.CalculationCallback {
            override fun onCalculationFinished() {
                runOnUiThread {
                    fillLayout()
                }
            }
        })

    }

    private fun fillLayout() {
        setBestGoals()
        setBestTime()
        setBestDuration()
        setBestPlaces()
        setBestBrightness()
        setBestNoise()
    }

    private fun setBestGoals() {
        val bestGoals = recommendationHelper.bestGoals
        if (bestGoals != null) {
            if (bestGoals.size > 0) {
                tv_best_goal_1.text = bestGoals[0].getGoalText()
//                tv_best_goal_1_percent.text = "${LearningSessionRepository.get(context!!).getLearningSessionsByGoalID(bestGoals[0].id)[0].userRating} %"
            }
            if (bestGoals.size > 1) {
                tv_best_goal_2.text = bestGoals[1].getGoalText()
//                tv_best_goal_2_percent.text = "${LearningSessionRepository.get(context!!).getLearningSessionsByGoalID(bestGoals[1].id)[0].userRating} %"
            }
            if (bestGoals.size > 2) {
                tv_best_goal_3.text = bestGoals[2].getGoalText()
//                tv_best_goal_3_percent.text = "${LearningSessionRepository.get(context!!).getLearningSessionsByGoalID(bestGoals[2].id)[0].userRating} %"
            }
        }

    }

    private fun setBestPlaces() {
        val bestPlaces = recommendationHelper.bestPlaces
        if (bestPlaces != null) {
            if (bestPlaces.isNotEmpty()) {
                tv_place1.text = bestPlaces[0].name
                tv_address1.text = bestPlaces[0].addressString
            }
            if (bestPlaces.size > 1) {
                tv_place2.text = bestPlaces[1].name
                tv_address2.text = bestPlaces[1].addressString
            }
        }
    }

    private fun setBestDuration() {
        val bestDuration = recommendationHelper.bestDuration
        if (bestDuration != 0)
            tv_best_duration_value.text = "$bestDuration minutes"
    }

    private fun setBestTime() {
        val bestTime = recommendationHelper.bestTime
        tv_best_time_value.text = bestTime
    }

    private fun setBestBrightness() {
        val bestBrightnessValue = recommendationHelper.bestBrightnessValue
        tv_brightness_value.text = "$bestBrightnessValue lux"
        tv_brightness_level.text = LightLevel.fromValue(bestBrightnessValue.toDouble()).name

    }

    private fun setBestNoise() {
        val bestNoiseValue = recommendationHelper.bestNoiseValue
        tv_noise_value.text = "$bestNoiseValue dB"
        tv_noise_level.text = NoiseLevel.fromValue(bestNoiseValue.toDouble()).name

    }
}
