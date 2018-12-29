package de.htwberlin.learningcompanion.recommendation

import android.content.Context
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.db.LearningSessionRepository
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.learning.SessionEvaluator
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

        return when {
            placeRatingElements.size >= 2 -> listOf(placeRatingElements[0].first, placeRatingElements[1].first)
            placeRatingElements.isNotEmpty() -> listOf(placeRatingElements[0].first)
            else -> listOf()
        }
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

    fun getBestDuration(): Int {
        val sessionsList = LearningSessionRepository.get(context).sessionsList

        // map which maps DURATION to USERRATING
        val mapWithDurations = hashMapOf<Int, Int>()

        val goalRepository = GoalRepository.get(context)

        sessionsList?.forEach {
            val durationInMin = goalRepository.getGoalByID(it.goalID).durationInMin

            mapWithDurations[durationInMin!!] = mapWithDurations[durationInMin]?.plus(it.userRating) ?: it.userRating
        }

        var bestDurationInMin = 0
        var bestUserRatingSum = 0

        val iterator = mapWithDurations.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value > bestUserRatingSum) {
                bestDurationInMin = entry.key
                bestUserRatingSum = entry.value
            }
        }

        return bestDurationInMin
    }

    fun getBestGoals(): List<Goal> {
        val goalsByDescendingUserRating = GoalRepository.get(context).getGoalsByDescendingUserRating()

        val endIndex = Math.min(goalsByDescendingUserRating.size, 3)

        return goalsByDescendingUserRating.subList(0, endIndex)
    }

    fun getBestBrightnessValue(): Int {
        val sessionsList = LearningSessionRepository.get(context).sessionsList

        // map which maps Light value average  to USERRATING
        val mapWithBrightnessLevels = hashMapOf<Double, Int>()

        sessionsList?.forEach {
            val lightAverage = SessionEvaluator.calculateAverage(it.lightValues)
            mapWithBrightnessLevels[lightAverage] = mapWithBrightnessLevels[lightAverage]?.plus(it.userRating) ?: it.userRating
        }

        var bestLightAverage = 0.0
        var bestUserRatingSum = 0

        val iterator = mapWithBrightnessLevels.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value > bestUserRatingSum) {
                bestLightAverage = entry.key
                bestUserRatingSum = entry.value
            }
        }

        return bestLightAverage.toInt()
    }

    fun getBestNoiseValue(): Int {
        val sessionsList = LearningSessionRepository.get(context).sessionsList

        // map which maps Noise value average to USERRATING
        val mapWithNoiseLevels = hashMapOf<Double, Int>()

        sessionsList?.forEach {
            val noiseAverage = SessionEvaluator.calculateAverage(it.noiseValues)
            mapWithNoiseLevels[noiseAverage] = mapWithNoiseLevels[noiseAverage]?.plus(it.userRating) ?: it.userRating
        }

        var bestNoiseAverage = 0.0
        var bestUserRatingSum = 0

        val iterator = mapWithNoiseLevels.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.value > bestUserRatingSum) {
                bestNoiseAverage = entry.key
                bestUserRatingSum = entry.value
            }
        }


        return bestNoiseAverage.toInt()
    }


}