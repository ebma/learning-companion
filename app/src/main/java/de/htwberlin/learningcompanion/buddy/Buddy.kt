package de.htwberlin.learningcompanion.buddy

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.model.Goal
import de.htwberlin.learningcompanion.model.Place
import java.util.*

const val MESSAGE_DURATION_IN_MILLIS = 10000L

class Buddy private constructor(private val context: Context) {

    private val buddyFaceHolder = BuddyFaceHolder.get(context)

    var isInDefaultState = true

    val drawableLiveData = MutableLiveData<Drawable>()
    val speechLiveData = MutableLiveData<String>()

    fun setNewRandomBuddyLearningText() {
        val randomArrayIndex = Random().nextInt(6)

        when (randomArrayIndex) {
            1 -> setRandomThinkingCharlie()
            2 -> setRandomSmilingCharlie()
            3 -> setRandomGrinningCharlie()
            4 -> setRandomGoofyCharlie()
            5 -> setRandomRelievedCharlie()
        }
    }

    fun setNewRandomBuddyBeforeLearningText() {
        showMessageForFixedAmount(getInfoText(), buddyFaceHolder.getDefaultFace())
    }

    fun setInstructionText() {
        showMessageForFixedAmount(getInfoText(), buddyFaceHolder.getDefaultFace())
    }

    fun showExitProhibitedMessage() {
        showMessageForFixedAmount(context.getString(R.string.exit_prohibited_message), buddyFaceHolder.getThinkingFace())
    }

    private fun showMessageForFixedAmount(message: String, drawable: Drawable?) {
        isInDefaultState = false
        speechLiveData.value = message
        drawableLiveData.value = drawable

        Handler().postDelayed({
            isInDefaultState = true
            speechLiveData.postValue("")
            drawableLiveData.postValue(buddyFaceHolder.getDefaultFace())
        }, MESSAGE_DURATION_IN_MILLIS)
    }

    private fun setRandomThinkingCharlie() {
        val stringArray = context.resources.getStringArray(R.array.buddy_thinking_sayings)
        val randomStringIndex = Random().nextInt(stringArray.size)

        showMessageForFixedAmount(stringArray[randomStringIndex], buddyFaceHolder.getThinkingFace())
    }

    private fun setRandomSmilingCharlie() {
        val stringArray = context.resources.getStringArray(R.array.buddy_smiling_sayings)
        val randomStringIndex = Random().nextInt(stringArray.size)

        showMessageForFixedAmount(stringArray[randomStringIndex], buddyFaceHolder.getSmilingFace())
    }

    private fun setRandomGoofyCharlie() {
        val stringArray = context.resources.getStringArray(R.array.buddy_goofy_sayings)
        val randomStringIndex = Random().nextInt(stringArray.size)

        showMessageForFixedAmount(stringArray[randomStringIndex], buddyFaceHolder.getGoofyFace())
    }

    private fun setRandomGrinningCharlie() {
        val stringArray = context.resources.getStringArray(R.array.buddy_grinning_sayings)
        val randomStringIndex = Random().nextInt(stringArray.size)

        showMessageForFixedAmount(stringArray[randomStringIndex], buddyFaceHolder.getGrinningFace())
    }

    private fun setRandomRelievedCharlie() {
        val stringArray = context.resources.getStringArray(R.array.buddy_relieved_sayings)
        val randomStringIndex = Random().nextInt(stringArray.size)

        showMessageForFixedAmount(stringArray[randomStringIndex], buddyFaceHolder.getCalmFace())
    }

    fun getInfoText(): String {
        val currentGoal = GoalRepository.get(context).getCurrentGoal()
        val currentPlace = PlaceRepository.get(context).getCurrentPlace()

        return when {
            currentGoal == null -> getGoalInfoText()
            currentPlace == null -> getPlaceInfoText()
            else -> getStartLearningInfoText(currentGoal, currentPlace)
        }
    }

    private fun getGoalInfoText(): String {
        return "Please press \"Menu\" and go to \"My Goals\" to set the goal that you want to achieve."
    }

    private fun getPlaceInfoText(): String {
        return "Please press \"Menu\" and go to \"My places\" to set the place where you want to learn."
    }

    private fun getStartLearningInfoText(currentGoal: Goal, currentPlace: Place): String {
        return "Your current goal is: \n${currentGoal.getGoalText()} \nYour current place is ${currentPlace.name}"
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: Buddy? = null

        fun get(context: Context): Buddy = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Buddy(context).also { INSTANCE = it }
        }
    }
}
