package de.htwberlin.learningcompanion.learning.evaluation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import de.htwberlin.learningcompanion.db.AppDatabase
import de.htwberlin.learningcompanion.model.LearningSession

class SessionOverviewViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var sessions: LiveData<List<LearningSession>>

    fun getLearningSessions(): LiveData<List<LearningSession>> {
        if (!::sessions.isInitialized) {
            loadLearningSessions()
        }
        return sessions
    }

    private fun loadLearningSessions() {
        sessions = AppDatabase.get(getApplication()).sessionDao().getLearningSessionsAsLiveData()
    }
}
