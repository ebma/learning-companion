package de.htwberlin.learningcompanion.charlie

import android.content.Context
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.db.PlaceRepository

class Charlie(private val context: Context) {

    fun getInfoText(): String {
        val currentGoal = GoalRepository.get(context).getCurrentGoal()
        val currentPlace = PlaceRepository.get(context).getCurrentPlace()

        return when {
            currentGoal == null -> getGoalInfoText()
            currentPlace == null -> getPlaceInfoText()
            else -> getStartLearningInfoText()
        }
    }

    private fun getGoalInfoText(): String {
        return "Please press \"Menu\" and go to \"My Goals\" to set the goal that you want to achieve."
    }

    private fun getPlaceInfoText(): String {
        return "Please press \"Menu\" and go to \"My places\" to set the place where you want to learn."
    }

    private fun getStartLearningInfoText(): String {
        // return "You can start your learning session by clicking on the \"Start\" Button below."
        val currentGoal = GoalRepository.get(context).getCurrentGoal()

        return "Your current goal is: \n${currentGoal!!.getGoalText()}"
    }

}