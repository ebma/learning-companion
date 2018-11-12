package de.htwberlin.learningcompanion.sensors

import android.util.Log
import de.htwberlin.learningcompanion.util.*

enum class LightLevel { LOWEST, LOW, MEDIUM, HIGH, HIGHEST }
enum class NoiseLevel { LOWEST, LOW, MEDIUM, HIGH, HIGHEST }

class LearningSessionEvaluator(private val lightValues: ArrayList<Float>,
                               private val noiseValues: ArrayList<Int>) {

    private val TAG = LearningSessionEvaluator::class.java.simpleName

    private var lightLevel: LightLevel = LightLevel.MEDIUM
    private var noiseLevel: NoiseLevel = NoiseLevel.MEDIUM

    public fun evaluateLight(): LightLevel {
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

    public fun evaluateNoise(): NoiseLevel {
        val noiseAverage = calculateAverage(noiseValues)

        when (noiseAverage) {
            in 0.0..NOISE_LOW_THRESHOLD -> noiseLevel = NoiseLevel.LOWEST
            in NOISE_LOW_THRESHOLD..NOISE_MEDIUM_THRESHOLD -> noiseLevel = NoiseLevel.LOW
            in NOISE_MEDIUM_THRESHOLD..NOISE_HIGH_THRESHOLD -> noiseLevel = NoiseLevel.MEDIUM
            in NOISE_HIGH_THRESHOLD..NOISE_HIGHEST_THRESHOLD -> noiseLevel = NoiseLevel.HIGH
            in NOISE_HIGHEST_THRESHOLD..Double.MAX_VALUE -> noiseLevel = NoiseLevel.HIGHEST
            else -> {
                Log.d(TAG, "error with evaluating noise")
            }
        }

        return noiseLevel
    }

    private fun <T : Number> calculateAverage(values: ArrayList<T>): Double {
        var sum = 0.0

        values.forEach { sum = sum.plus(it.toFloat()) }

        return sum / values.size
    }

}