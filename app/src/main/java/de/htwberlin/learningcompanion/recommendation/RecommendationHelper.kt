package de.htwberlin.learningcompanion.recommendation

import android.content.Context
import de.htwberlin.learningcompanion.model.Goal
import de.htwberlin.learningcompanion.model.Place

class RecommendationHelper(private val context: Context) {

    fun getBestPlaces(): List<Place> {
        return listOf()
    }

    fun getBestTime(): String {
        return ""
    }

    fun getBestDuration(): String {
        return ""
    }

    fun getBestGoals(): List<Goal> {
        return listOf()
    }

    fun getBestBrightnessValue(): Int {
        return 0
    }

    fun getBestNoiseValue(): Int {
        return 0
    }
}