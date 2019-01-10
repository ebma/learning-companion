package de.htwberlin.learningcompanion.buddy

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.util.SharedPreferencesHelper

class BuddyFaceHolder private constructor(private val context: Context) {

    val sharedPreferencesHelper = SharedPreferencesHelper.get(context)

    fun getGoofyFace(): Drawable? {
        val buddyColor = sharedPreferencesHelper.getBuddyColor()

        return when (buddyColor) {
            Color.BLUE -> context.getDrawable(R.drawable.ic_blue_goofy)
            Color.GREEN -> context.getDrawable(R.drawable.ic_green_goofy)
            Color.MAGENTA -> context.getDrawable(R.drawable.ic_green_goofy_glasses)
            Color.YELLOW -> context.getDrawable(R.drawable.ic_orange_goofy)
            Color.RED -> context.getDrawable(R.drawable.ic_orange_goofy)
            else -> context.getDrawable(R.drawable.ic_blue_goofy)
        }
    }

    fun getCalmFace(): Drawable? {
        val buddyColor = sharedPreferencesHelper.getBuddyColor()

        return when (buddyColor) {
            Color.BLUE -> context.getDrawable(R.drawable.ic_blue_calm)
            Color.GREEN -> context.getDrawable(R.drawable.ic_green_calm)
            Color.MAGENTA -> context.getDrawable(R.drawable.ic_green_calm_glasses)
            Color.YELLOW -> context.getDrawable(R.drawable.ic_orange_calm)
            Color.RED -> context.getDrawable(R.drawable.ic_orange_calm)
            else -> context.getDrawable(R.drawable.ic_blue_calm)
        }
    }

    fun getGrinningFace(): Drawable? {
        val buddyColor = sharedPreferencesHelper.getBuddyColor()

        return when (buddyColor) {
            Color.BLUE -> context.getDrawable(R.drawable.ic_blue_grinning)
            Color.GREEN -> context.getDrawable(R.drawable.ic_green_grinning)
            Color.MAGENTA -> context.getDrawable(R.drawable.ic_green_grinning_glasses)
            Color.YELLOW -> context.getDrawable(R.drawable.ic_orange_grinning)
            Color.RED -> context.getDrawable(R.drawable.ic_orange_grinning)
            else -> context.getDrawable(R.drawable.ic_blue_grinning)
        }
    }

    fun getSmilingFace(): Drawable? {
        val buddyColor = sharedPreferencesHelper.getBuddyColor()

        return when (buddyColor) {
            Color.BLUE -> context.getDrawable(R.drawable.ic_blue_smile)
            Color.GREEN -> context.getDrawable(R.drawable.ic_green_smile)
            Color.MAGENTA -> context.getDrawable(R.drawable.ic_green_smile_glasses)
            Color.YELLOW -> context.getDrawable(R.drawable.ic_orange_smile)
            Color.RED -> context.getDrawable(R.drawable.ic_orange_smile)
            else -> context.getDrawable(R.drawable.ic_blue_smile)
        }
    }

    fun getThinkingFace(): Drawable? {
        val buddyColor = sharedPreferencesHelper.getBuddyColor()

        return when (buddyColor) {
            Color.BLUE -> context.getDrawable(R.drawable.ic_blue_thinking)
            Color.GREEN -> context.getDrawable(R.drawable.ic_green_thinking)
            Color.MAGENTA -> context.getDrawable(R.drawable.ic_green_thinking_glasses)
            Color.YELLOW -> context.getDrawable(R.drawable.ic_orange_thinking)
            Color.RED -> context.getDrawable(R.drawable.ic_orange_thinking)
            else -> context.getDrawable(R.drawable.ic_blue_thinking)
        }
    }

    fun getDefaultFace(): Drawable? {
        val buddyColor = sharedPreferencesHelper.getBuddyColor()

        return when (buddyColor) {
            Color.BLUE -> context.getDrawable(R.drawable.ic_blue_smile)
            Color.GREEN -> context.getDrawable(R.drawable.ic_green_smile)
            Color.MAGENTA -> context.getDrawable(R.drawable.ic_green_smile_glasses)
            Color.YELLOW -> context.getDrawable(R.drawable.ic_orange_smile)
            Color.RED -> context.getDrawable(R.drawable.ic_orange_smile)
            else -> context.getDrawable(R.drawable.ic_blue_smile)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: BuddyFaceHolder? = null

        fun get(context: Context): BuddyFaceHolder = INSTANCE ?: synchronized(this) {
            INSTANCE ?: BuddyFaceHolder(context).also { INSTANCE = it }
        }
    }
}