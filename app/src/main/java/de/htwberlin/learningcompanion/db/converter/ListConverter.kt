package de.htwberlin.learningcompanion.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class ListConverter {

    @TypeConverter
    fun fromIntString(value: String): ArrayList<Int> {
        val listType = object : TypeToken<ArrayList<Int>>() {

        }.type
        return Gson().fromJson<Any>(value, listType) as ArrayList<Int>
    }

    @TypeConverter
    fun fromIntArrayList(list: ArrayList<Int>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromFloatString(value: String): ArrayList<Float> {
        val listType = object : TypeToken<ArrayList<Float>>() {

        }.type
        return Gson().fromJson<Any>(value, listType) as ArrayList<Float>
    }

    @TypeConverter
    fun fromFloatArrayList(list: ArrayList<Float>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}