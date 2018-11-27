package de.htwberlin.learningcompanion.db.converter

import androidx.room.TypeConverter
import de.htwberlin.learningcompanion.learning.NoiseLevel

class NoiseLevelTypeConverter {

    @TypeConverter
    fun toNoiseLevel(value: Int?): NoiseLevel? {
        return if (value == null) null else NoiseLevel.values()[value]
    }

    @TypeConverter
    fun toInt(value: NoiseLevel?): Int? {
        return value?.ordinal
    }
}