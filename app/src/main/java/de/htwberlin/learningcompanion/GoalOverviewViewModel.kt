package de.htwberlin.learningcompanion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import de.htwberlin.learningcompanion.db.AppDatabase
import de.htwberlin.learningcompanion.model.Goal

class GoalOverviewViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var goals: LiveData<List<Goal>>

    fun getGoals(): LiveData<List<Goal>> {
        if (!::goals.isInitialized) {
            loadGoals()
        }
        return goals
    }

    private fun loadGoals() {
        goals = AppDatabase.get(getApplication()).goalDao().getGoalsAsLiveData()
    }
}
