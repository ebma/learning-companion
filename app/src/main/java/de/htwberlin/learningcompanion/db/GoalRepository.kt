package de.htwberlin.learningcompanion.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import de.htwberlin.learningcompanion.model.Goal
import java.util.concurrent.Executors

class GoalRepository private constructor(context: Context) {

    private val context: Context = context.applicationContext

    var goalsLiveData: LiveData<List<Goal>>
    var goalsList: List<Goal>? = null

    private val appDatabase: AppDatabase
    private val executor = Executors.newSingleThreadExecutor()

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

    fun getGoalByID(goalID: Int): Goal {
        return appDatabase.goalDao().getGoalByID(goalID)
    }

    fun getCurrentGoal(): Goal? {
        return appDatabase.goalDao().getCurrentGoal()
    }

    fun insertGoalList(goalList: List<Goal>) {
        executor.execute { appDatabase.goalDao().insertGoals(goalList) }
    }

    fun insertGoal(goal: Goal) {
        executor.execute { appDatabase.goalDao().insertGoal(goal) }
    }

    fun deleteGoal(goal: Goal) {
        executor.execute { appDatabase.goalDao().deleteGoal(goal) }
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
