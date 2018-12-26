package de.htwberlin.learningcompanion.recommendation

import android.content.Context
import de.htwberlin.learningcompanion.db.LearningSessionRepository
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.model.Goal
import de.htwberlin.learningcompanion.model.LearningSession
import de.htwberlin.learningcompanion.model.Place
import java.util.*

class RecommendationHelper(private val context: Context) {

    fun getBestPlaces(): List<Place> {
        val placesList = PlaceRepository.get(context).placesList

        val placeRatingMap = hashMapOf<Place, Double>()

        placesList?.forEach { placeRatingMap[it] = calculateAverageUserRatingForPlace(it) }

        val sortedPlaceRating = placeRatingMap.toList().sortedBy { (_, value) -> value }.toMap()
        val placeRatingElements = sortedPlaceRating.toList()

        return listOf(placeRatingElements[0].first, placeRatingElements[1].first)
    }

    private fun calculateAverageUserRatingForPlace(place: Place): Double {
        val sessionsList = LearningSessionRepository.get(context).sessionsList

        var placeRatingSum = 0.0
        var count = 0
        sessionsList?.forEach {
            if (it.placeID == place.id) {
                placeRatingSum += it.userRating
                count++
            }
        }

        return placeRatingSum / count
    }

    fun getBestTime(): String {
        val sessionsList = LearningSessionRepository.get(context).sessionsList

        val morningSessions = mutableListOf<LearningSession>()
        val afternoonSessions = mutableListOf<LearningSession>()
        val eveningSessions = mutableListOf<LearningSession>()
        val nightOwlSessions = mutableListOf<LearningSession>()
        sessionsList?.forEach {
            val instance = Calendar.getInstance()
            instance.time = it.createdAt

            val hour = instance.get(Calendar.HOUR_OF_DAY)

            when (hour) {
                in 6..12 -> morningSessions.add(it)
                in 12..18 -> afternoonSessions.add(it)
                in 18..24 -> eveningSessions.add(it)
                in 0..6 -> nightOwlSessions.add(it)
            }
        }

        val averageMorningRating = calculateAverageUserRatingForSessionList(morningSessions)
        val averageAfternoonRating = calculateAverageUserRatingForSessionList(afternoonSessions)
        val averageEveningRating = calculateAverageUserRatingForSessionList(eveningSessions)
        val averageNightOwlRating = calculateAverageUserRatingForSessionList(nightOwlSessions)

        if (averageMorningRating >= averageAfternoonRating
                && averageMorningRating >= averageEveningRating
                && averageMorningRating >= averageNightOwlRating)
            return "Morning (06-12)"
        else if (averageAfternoonRating >= averageMorningRating
                && averageAfternoonRating >= averageEveningRating
                && averageAfternoonRating >= averageNightOwlRating)
            return "Afternoon (12-18)"
        else if (averageEveningRating >= averageAfternoonRating
                && averageEveningRating >= averageMorningRating
                && averageEveningRating >= averageNightOwlRating)
            return "Evening (18-24)"
        else if (averageNightOwlRating >= averageMorningRating
                && averageNightOwlRating >= averageEveningRating
                && averageNightOwlRating >= averageAfternoonRating)
            return "Night owl (00-06)"
        else
            return ""
    }

    private fun calculateAverageUserRatingForSessionList(sessions: List<LearningSession>): Double {
        var ratingSum = 0.0
        var count = 0

        sessions.forEach {
            ratingSum += it.userRating
            count++
        }

        return ratingSum / count
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