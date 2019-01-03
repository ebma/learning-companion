package de.htwberlin.learningcompanion

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContextCompat

class MyPreference constructor (context: Context) {

    val PREFERENCE_NAME = "CharlieSharedPreference"

    // 1 -> gentle, 2 -> nerdy, 3 -> calm, 4 -> happy, 5 -> goofy
    val PREFERENCE_CHARLIE_NUMBER = "CharlieNumber" // entspricht -> gentle Charlie
    val PREFERENCE_USER_NAME = "UserName"
    val PREFERENCE_BUDDY_NAME = "BuddyName"

    val INTERVAL = 30
    val FREQUENCY = 1

    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)


    fun getCharlieNumber(): Int {
        return preference.getInt(PREFERENCE_CHARLIE_NUMBER, 1)
    }

    fun getUserName() : String {
        return preference.getString(PREFERENCE_USER_NAME, "You")
    }

    fun getBuddyName(): String {
        return preference.getString(PREFERENCE_BUDDY_NAME, "Charlie")
    }

    fun setCharlieName(charlieNumber: Int) {
        val editor = preference.edit()
        editor.putInt(PREFERENCE_CHARLIE_NUMBER, charlieNumber)
        editor.apply()
    }

    fun setUserName(userName: String) {
        val editor = preference.edit()
        editor.putString(PREFERENCE_USER_NAME, userName)
        editor.apply()
    }

    fun setBuddyName(buddyName: String) {
        val editor = preference.edit()
        editor.putString(PREFERENCE_BUDDY_NAME, buddyName)
        editor.apply()
    }

}