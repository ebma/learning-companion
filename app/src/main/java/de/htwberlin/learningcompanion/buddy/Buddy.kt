package de.htwberlin.learningcompanion.buddy

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.db.PlaceRepository
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class Buddy(private val context: Context) {

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    fun getInfoText(): String {
        val currentGoal = GoalRepository.get(context).getCurrentGoal()
        val currentPlace = PlaceRepository.get(context).getCurrentPlace()

        return when {
            currentGoal == null -> getGoalInfoText()
            currentPlace == null -> getPlaceInfoText()
            else -> getStartLearningInfoText()
        }
    }

    fun bindViews(imageView: ImageView, textView: TextView) {
        this.imageView = imageView
        this.textView = textView

        imageView.onClick { showNewBuddyText() }
    }

    fun showNewBuddyText() {
        val randomArrayIndex = Random().nextInt(6)

        when (randomArrayIndex) {
            1 -> setRandomThinkingCharlie()
            2 -> setRandomSmilingCharlie()
            3 -> setRandomGrinningCharlie()
            4 -> setRandomGoofyCharlie()
            5 -> setRandomRelievedCharlie()
        }
    }

    private fun setRandomThinkingCharlie() {
        val stringArray = context.resources.getStringArray(R.array.buddy_thinking_sayings)
        val randomStringIndex = Random().nextInt(stringArray.size)

        textView.text = stringArray[randomStringIndex]
        imageView.setImageDrawable(context.getDrawable(R.drawable.blue_charlie_thinking))
    }

    private fun setRandomSmilingCharlie() {
        val stringArray = context.resources.getStringArray(R.array.buddy_smiling_sayings)
        val randomStringIndex = Random().nextInt(stringArray.size)

        textView.text = stringArray[randomStringIndex]
        imageView.setImageDrawable(context.getDrawable(R.drawable.blue_charlie_smiling))
    }

    private fun setRandomGoofyCharlie() {
        val stringArray = context.resources.getStringArray(R.array.buddy_goofy_sayings)
        val randomStringIndex = Random().nextInt(stringArray.size)

        textView.text = stringArray[randomStringIndex]
        imageView.setImageDrawable(context.getDrawable(R.drawable.blue_charlie_goofy))
    }

    private fun setRandomGrinningCharlie() {
        val stringArray = context.resources.getStringArray(R.array.buddy_grinning_sayings)
        val randomStringIndex = Random().nextInt(stringArray.size)

        textView.text = stringArray[randomStringIndex]
        imageView.setImageDrawable(context.getDrawable(R.drawable.blue_charlie_grinning))
    }

    private fun setRandomRelievedCharlie() {
        val stringArray = context.resources.getStringArray(R.array.buddy_relieved_sayings)
        val randomStringIndex = Random().nextInt(stringArray.size)

        textView.text = stringArray[randomStringIndex]
        imageView.setImageDrawable(context.getDrawable(R.drawable.blue_charlie_relieved))
    }

    private fun getGoalInfoText(): String {
        return "Please press \"Menu\" and go to \"My Goals\" to set the goal that you want to achieve."
    }

    private fun getPlaceInfoText(): String {
        return "Please press \"Menu\" and go to \"My places\" to set the place where you want to learn."
    }

    private fun getStartLearningInfoText(): String {
        // return "You can start your learning session by clicking on the \"Start\" Button below."
        val currentGoal = GoalRepository.get(context).getCurrentGoal()

        return "Your current goal is: \n${currentGoal!!.getGoalText()}"
    }

}