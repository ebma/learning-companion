package de.htwberlin.learningcompanion.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import de.htwberlin.learningcompanion.model.LearningSession

class LearningSessionRepository private constructor(context: Context) {

    private val context: Context = context.applicationContext

    var sessionsLiveData: LiveData<List<LearningSession>>
    var sessionsList: List<LearningSession>? = null

    private val appDatabase: AppDatabase

    private val allLearningSessions: LiveData<List<LearningSession>>
        get() = appDatabase.sessionDao().getLearningSessionsAsLiveData()

    init {
        appDatabase = AppDatabase.get(this.context)
        sessionsList = appDatabase.sessionDao().getLearningSessions()
        sessionsLiveData = allLearningSessions

        sessionsLiveData.observeForever { sessions -> sessionsList = sessions }
    }

    fun getLearningSessionByID(sessionID: Long): LearningSession {
        return appDatabase.sessionDao().getLearningSessionByID(sessionID)
    }

    fun getLearningSessionByGoalAndPlaceID(goalID: Long, placeID: Long): List<LearningSession> {
        return appDatabase.sessionDao().getLearningSessionByGoalAndPlaceID(goalID, placeID)
    }

    fun getNewestLearningSession(): LearningSession {
        return appDatabase.sessionDao().getNewestLearningSession()
    }

    fun getLearningSessionsByGoalID(goalID: Long): List<LearningSession> {
        return appDatabase.sessionDao().getLearningSessionByGoalID(goalID)
    }

    fun getLearningSessionsByPlaceID(placeID: Long): List<LearningSession> {
        return appDatabase.sessionDao().getLearningSessionByPlaceID(placeID)
    }

    fun insertLearningSession(session: LearningSession) {
        appDatabase.sessionDao().insertLearningSession(session)
    }

    fun updateLearningSession(session: LearningSession) {
        appDatabase.sessionDao().updateLearningSession(session)
    }

    fun deleteLearningSession(session: LearningSession) {
        appDatabase.sessionDao().deleteLearningSession(session)
    }

    companion object {
        private val TAG = LearningSessionRepository::class.java.simpleName

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: LearningSessionRepository? = null

        fun get(context: Context): LearningSessionRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: LearningSessionRepository(context).also { INSTANCE = it }
        }
    }
}
