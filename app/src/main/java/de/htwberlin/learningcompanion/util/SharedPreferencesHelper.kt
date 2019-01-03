package de.htwberlin.learningcompanion.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper private constructor(context: Context) {

    private val SHARED_PREF_NAME = "preferences"

    private val DEFAULT_USER_NAME = "You"
    private val DEFAULT_COMPANION_NAME = "Charlie"

    private val sharedPref: SharedPreferences

    init {
        sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    fun getBuddyMood(): String {
        val charlieNum = sharedPref.getInt("MoodNumber", 0)

        return when (charlieNum) {
            1 -> "gentle"
            2 -> "nerdy"
            3 -> "calm"
            4 -> "happy"
            5 -> "goofy"
            else -> {
                "gentle"
            }
        }
    }

    fun setBuddyMood(moodNumber: Int) {
        val editor = sharedPref.edit()
        editor.putInt("MoodNumber", moodNumber)
        editor.apply()
    }

    fun getUserName(): String {
        return sharedPref.getString("UserName", DEFAULT_USER_NAME)!!
    }

    fun setUserName(userName: String) {
        val editor = sharedPref.edit()
        editor.putString("UserName", userName)
        editor.apply()
    }

    fun getBuddyName(): String {
        return sharedPref.getString("BuddyName", DEFAULT_COMPANION_NAME)!!
    }

    fun setBuddyName(buddyName: String) {
        val editor = sharedPref.edit()
        editor.putString("BuddyName", buddyName)
        editor.apply()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: SharedPreferencesHelper? = null

        fun get(context: Context): SharedPreferencesHelper = INSTANCE ?: synchronized(this) {
            INSTANCE ?: SharedPreferencesHelper(context).also { INSTANCE = it }
        }
    }
}