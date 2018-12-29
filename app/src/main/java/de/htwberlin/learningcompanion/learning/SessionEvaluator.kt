package de.htwberlin.learningcompanion.learning

import de.htwberlin.learningcompanion.util.*

enum class LightLevel { LOWEST, LOW, MEDIUM, HIGH, HIGHEST;

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

enum class NoiseLevel { LOWEST, LOW, MEDIUM, HIGH, HIGHEST;

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
                       private val noiseValues: ArrayList<Int>) {

    private val TAG = SessionEvaluator::class.java.simpleName

    private var lightLevel: LightLevel = LightLevel.MEDIUM
    private var noiseLevel: NoiseLevel = NoiseLevel.MEDIUM

    public fun evaluateLight(): LightLevel {
        val lightAverage = calculateAverage(lightValues)

        lightLevel = LightLevel.fromValue(lightAverage)

        return lightLevel
    }

    public fun evaluateNoise(): NoiseLevel {
        val noiseAverage = calculateAverage(noiseValues)

        noiseLevel = NoiseLevel.fromValue(noiseAverage)

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