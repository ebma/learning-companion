package de.htwberlin.learningcompanion.db

import androidx.lifecycle.LiveData
import androidx.room.*
import de.htwberlin.learningcompanion.model.Goal

@Dao
interface GoalDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGoal(goal: Goal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGoals(goal: List<Goal>)

    @Update
    fun updateGoal(goal: Goal)

    @Update
    fun updateGoals(goals: List<Goal>)

    @Delete
    fun deleteGoal(goal: Goal)

    @Query("SELECT * FROM goals WHERE id == :id")
    fun getGoalByID(id: Long): Goal

    @Query("SELECT * FROM goals")
    fun getGoals(): List<Goal>

    @Query("SELECT * FROM goals WHERE currentGoal == 1")
    fun getCurrentGoal(): Goal?

    @Query("SELECT * FROM goals")
    fun getGoalsAsLiveData(): LiveData<List<Goal>>
}