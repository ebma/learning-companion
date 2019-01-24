package de.htwberlin.learningcompanion.learning

import de.htwberlin.learningcompanion.util.*

enum class LightLevel(val levelName: String) {
    LOWEST("Dark"), LOW("Dim"), MEDIUM("Medium"), HIGH("Bright"), HIGHEST("Shiny");

    companion object {
        fun fromValue(value: Double): LightLevel {
            return when (value) {
                in 0.0..LIGHT_LOW_THRESHOLD -> LightLevel.LOWEST
                in LIGHT_LOW_THRESHOLD..LIGHT_MEDIUM_THRESHOLD -> LightLevel.LOW
                in LIGHT_MEDIUM_THRESHOLD..LIGHT_HIGH_THRESHOLD -> LightLevel.MEDIUM
                in LIGHT_HIGH_THRESHOLD..LIGHT_HIGHEST_THRESHOLD -> LightLevel.HIGH
                in LIGHT_HIGHEST_THRESHOLD..Double.MAX_VALUE -> LightLevel.HIGHEST
                else -> {
                    LightLevel.LOWEST
                }
            }
        }
    }
}

enum class NoiseLevel(val levelName: String) {
    LOWEST("Silent"), LOW("Quite"), MEDIUM("Medium"), HIGH("Loud"), HIGHEST("Noisy");

    companion object {
        fun fromValue(value: Double): NoiseLevel {
            return when (value) {
                in 0.0..NOISE_LOW_THRESHOLD -> NoiseLevel.LOWEST
                in NOISE_LOW_THRESHOLD..NOISE_MEDIUM_THRESHOLD -> NoiseLevel.LOW
                in NOISE_MEDIUM_THRESHOLD..NOISE_HIGH_THRESHOLD -> NoiseLevel.MEDIUM
                in NOISE_HIGH_THRESHOLD..NOISE_HIGHEST_THRESHOLD -> NoiseLevel.HIGH
                in NOISE_HIGHEST_THRESHOLD..Double.MAX_VALUE -> NoiseLevel.HIGHEST
                else -> {
                    NoiseLevel.LOWEST
                }
            }
        }
    }
}

class SessionEvaluator(private val lightValues: ArrayList<Float>,
                       private val noiseValues: ArrayList<Float>) {

    private val TAG = SessionEvaluator::class.java.simpleName

    private var lightLevel: LightLevel = LightLevel.MEDIUM
    private var noiseLevel: NoiseLevel = NoiseLevel.MEDIUM

    fun evaluateLight(): LightLevel {
        lightLevel = LightLevel.fromValue(lightValues.last().toDouble())
        return lightLevel
    }

    fun evaluateNoise(): NoiseLevel {
        noiseLevel = NoiseLevel.fromValue(noiseValues.last().toDouble())
        return noiseLevel
    }

    companion object {
        fun <T : Number> calculateAverage(values: ArrayList<T>): Double {
            var sum = 0.0

            values.forEach { sum = sum.plus(it.toFloat()) }

            return sum / values.size
        }
    }

}