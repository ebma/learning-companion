package de.htwberlin.learningcompanion.db

import androidx.lifecycle.LiveData
import androidx.room.*
import de.htwberlin.learningcompanion.model.Goal

@Dao
interface GoalDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGoal(goal: Goal)

    @Update
    fun updateGoal(goal: Goal)

    @Delete
    fun deleteGoal(goal: Goal)

    @Query("SELECT * FROM goals WHERE id == :id")
    fun getGoalByID(id: Int): List<Goal>

    @Query("SELECT * FROM goals")
    fun getGoals(): List<Goal>

    @Query("SELECT * FROM goal")
    fun getGoalsAsLiveData(): LiveData<List<Goal>>
}