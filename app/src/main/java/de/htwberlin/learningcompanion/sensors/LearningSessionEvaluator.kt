package de.htwberlin.learningcompanion.sensors

import android.util.Log
import de.htwberlin.learningcompanion.util.LIGHT_HIGHEST_THRESHOLD
import de.htwberlin.learningcompanion.util.LIGHT_HIGH_THRESHOLD
import de.htwberlin.learningcompanion.util.LIGHT_LOW_THRESHOLD
import de.htwberlin.learningcompanion.util.LIGHT_MEDIUM_THRESHOLD

enum class LightLevel { LOWEST, LOW, MEDIUM, HIGH, HIGHEST }
enum class NoiseLevel { LOWEST, LOW, MEDIUM, HIGH, HIGHEST }

class LearningSessionEvaluator(private val lightValues: ArrayList<Float>,
                               private val noiseValues: ArrayList<Float>) {

    private val TAG = LearningSessionEvaluator::class.java.simpleName

    private var lightLevel: LightLevel = LightLevel.MEDIUM
    private var noiseLevel: NoiseLevel = NoiseLevel.MEDIUM

    private fun evaluateLight(): LightLevel {
        val lightAverage = calculateAverage(lightValues)

        when (lightAverage) {
            in 0.0..LIGHT_LOW_THRESHOLD -> lightLevel = LightLevel.LOWEST
            in LIGHT_LOW_THRESHOLD..LIGHT_MEDIUM_THRESHOLD -> lightLevel = LightLevel.LOW
            in LIGHT_MEDIUM_THRESHOLD..LIGHT_HIGH_THRESHOLD -> lightLevel = LightLevel.MEDIUM
            in LIGHT_HIGH_THRESHOLD..LIGHT_HIGHEST_THRESHOLD -> lightLevel = LightLevel.HIGH
            in LIGHT_HIGHEST_THRESHOLD..Double.MAX_VALUE -> lightLevel = LightLevel.HIGHEST
            else -> {
                Log.d(TAG, "error with evaluating light")
            }
        }

        return lightLevel
    }

    private fun evaluateNoise(): NoiseLevel {
        val noiseAverage = calculateAverage(noiseValues)

        return NoiseLevel.HIGH
    }

    private fun calculateAverage(values: ArrayList<Float>): Double {
        var sum = 0.0

        values.forEach { sum += it }

        return sum / values.size
    }

}