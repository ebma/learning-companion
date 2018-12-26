package de.htwberlin.learningcompanion.recommendation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.R
import kotlinx.android.synthetic.main.fragment_recommendation.*

class RecommendationFragment : Fragment() {

    private lateinit var recommendationHelper: RecommendationHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recommendation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recommendationHelper = RecommendationHelper(context!!)

        fillLayout()
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
        val bestGoals = recommendationHelper.getBestGoals()
        if (bestGoals.size > 0)
            tv_best_goal_1.text = bestGoals[0].getGoalText()
        if (bestGoals.size > 1)
            tv_best_goal_2.text = bestGoals[1].getGoalText()
        if (bestGoals.size > 2)
            tv_best_goal_3.text = bestGoals[2].getGoalText()

    }

    private fun setBestDuration() {
        val bestDuration = recommendationHelper.getBestDuration()
        if (bestDuration.isNotBlank())
            tv_best_duration_value.text = "$bestDuration minutes"
    }

    private fun setBestTime() {
        val bestTime = recommendationHelper.getBestTime()
        tv_best_time_value.text = bestTime
    }

    private fun setBestPlaces() {
        val bestPlaces = recommendationHelper.getBestPlaces()
        if (bestPlaces.isNotEmpty()) {
            tv_place1.text = bestPlaces[0].name
            tv_address1.text = bestPlaces[0].addressString
        }
        if (bestPlaces.size > 1) {
            tv_place2.text = bestPlaces[1].name
            tv_address2.text = bestPlaces[1].addressString
        }
    }

    private fun setBestBrightness() {
        val bestBrightnessValue = recommendationHelper.getBestBrightnessValue()
        if (bestBrightnessValue != 0) {
            tv_brightness_value.text = "$bestBrightnessValue lux"
            tv_brightness_level.text = ""
        }
    }

    private fun setBestNoise() {
        val bestNoiseValue = recommendationHelper.getBestNoiseValue()
        if (bestNoiseValue != 0) {
            tv_noise_value.text = "$bestNoiseValue dB"
            tv_noise_level.text = ""
        }
    }


}
