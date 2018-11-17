package de.htwberlin.learningcompanion.learning

import android.app.Activity
import android.os.CountDownTimer
import android.util.Log
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.model.Goal
import org.jetbrains.anko.sensorManager
import java.util.concurrent.TimeUnit


class SessionHandler(private val activity: Activity) {

    companion object {
        private val TAG = SessionHandler::class.java.simpleName
        private val INTERVAL_IN_SECONDS = 5 // maybe 1/min soon
        private val ON_UPDATE = 1
        private val ON_FINISH = 2
    }

    private val observerList = mutableListOf<LearningSessionObserver>()

    private lateinit var goal: Goal
    private lateinit var timer: CountDownTimer

    private var goalTargetDurationInMin = 0
    private var remainingMillis = 0L

    private val sensorHandler = SensorHandler(activity.sensorManager)
    private val learningSessionEvaluator = LearningSessionEvaluator(sensorHandler.lightDataList, sensorHandler.noiseDataList)

    fun canStartLearningSession(): Boolean {
        val currentGoal = GoalRepository.get(activity).getCurrentGoal()
        val currentPlace = PlaceRepository.get(activity).getCurrentPlace()

        return when {
            currentGoal == null -> false
            currentPlace == null -> false
            else -> true
        }
    }

    fun startLearningSession() {
        retrieveInformationFromGoal()
        startTimer()

        sensorHandler.clear()
        sensorHandler.start(INTERVAL_IN_SECONDS)
    }

    private fun retrieveInformationFromGoal() {
        goal = GoalRepository.get(activity).getCurrentGoal()!!
        goalTargetDurationInMin = goal.durationInMin ?: 0
    }

    private fun startTimer() {
        timer = object : CountDownTimer(TimeUnit.MINUTES.toMillis(goalTargetDurationInMin.toLong()), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingMillis = millisUntilFinished
                notifyObservers(ON_UPDATE)
                Log.d(TAG, "tick called")
            }

            override fun onFinish() {
                notifyObservers(ON_FINISH)
            }
        }

        timer.start()
    }

    fun observe(observer: LearningSessionObserver) {
        observerList.add(observer)
    }

    private fun notifyObservers(notifyCase: Int) {
        if (notifyCase == ON_UPDATE)
            observerList.forEach {
                it.onUpdate(remainingMillis)
            }
        else if (notifyCase == ON_FINISH)
            observerList.forEach {
                it.onFinish()
            }
    }

    fun stopLearningSession() {
        sensorHandler.stop()
        timer.cancel()
    }

    fun getSessionInfo(): String {
        val lightLevel = learningSessionEvaluator.evaluateLight()
        val noiseLevel = learningSessionEvaluator.evaluateNoise()

        val timeString = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(remainingMillis), TimeUnit.MILLISECONDS.toMinutes(remainingMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(remainingMillis)), TimeUnit.MILLISECONDS.toSeconds(remainingMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingMillis)))

        return "Light: $lightLevel \nNoise: $noiseLevel \nRemaining time: $timeString"
    }

    interface LearningSessionObserver {
        fun onUpdate(millisUntilFinished: Long)
        fun onFinish()
    }
}