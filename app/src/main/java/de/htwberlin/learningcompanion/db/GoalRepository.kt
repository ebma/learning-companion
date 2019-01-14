package de.htwberlin.learningcompanion.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import de.htwberlin.learningcompanion.model.Goal

class GoalRepository private constructor(context: Context) {

    private val context: Context = context.applicationContext

    var goalsLiveData: LiveData<List<Goal>>
    var goalsList: List<Goal>? = null

    private val appDatabase: AppDatabase

    private val allGoals: LiveData<List<Goal>>
        get() = appDatabase.goalDao().getGoalsAsLiveData()

    init {
        appDatabase = AppDatabase.get(this.context)
        goalsLiveData = allGoals

        goalsLiveData.observeForever { goals -> goalsList = goals }
    }

    fun setGoalAsCurrentGoal(goal: Goal) {
        val goals = appDatabase.goalDao().getGoals()

        goals.forEach {
            it.currentGoal = it.id == goal.id
        }

        appDatabase.goalDao().updateGoals(goals)
    }

    fun setNoGoalAsCurrentGoal() {
        val goals = appDatabase.goalDao().getGoals()

        goals.forEach {
            it.currentGoal = false
        }

        appDatabase.goalDao().updateGoals(goals)
    }

    fun getGoalByID(goalID: Long): Goal {
        return appDatabase.goalDao().getGoalByID(goalID)
    }

    fun getCurrentGoal(): Goal? {
        return appDatabase.goalDao().getCurrentGoal()
    }

    fun insertGoalList(goalList: List<Goal>) {
        appDatabase.goalDao().insertGoals(goalList)
    }

    fun insertGoal(goal: Goal) {
        appDatabase.goalDao().insertGoal(goal)
    }

    fun updateGoal(goal: Goal) {
        appDatabase.goalDao().updateGoal(goal)
    }

    fun deleteGoal(goal: Goal) {
        appDatabase.goalDao().deleteGoal(goal)
    }

    companion object {
        private val TAG = GoalRepository::class.java.simpleName

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: GoalRepository? = null

        fun get(context: Context): GoalRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: GoalRepository(context).also { INSTANCE = it }
        }
    }
}
