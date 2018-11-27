package de.htwberlin.learningcompanion.db

import androidx.lifecycle.LiveData
import androidx.room.*
import de.htwberlin.learningcompanion.model.LearningSession

@Dao
interface LearningSessionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLearningSession(session: LearningSession)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLearningSessions(places: List<LearningSession>)

    @Update
    fun updateLearningSession(session: LearningSession)

    @Update
    fun updateLearningSessions(places: List<LearningSession>)

    @Delete
    fun deleteLearningSession(session: LearningSession)

    @Query("SELECT * FROM sessions WHERE id == :id")
    fun getLearningSessionByID(id: Long): LearningSession

    @Query("SELECT * FROM sessions WHERE place_id == :id")
    fun getLearningSessionByPlaceID(id: Long): List<LearningSession>

    @Query("SELECT * FROM sessions WHERE goal_id == :id")
    fun getLearningSessionByGoalID(id: Long): List<LearningSession>

    @Query("SELECT * FROM sessions")
    fun getLearningSessions(): List<LearningSession>

    @Query("SELECT * FROM sessions")
    fun getLearningSessionsAsLiveData(): LiveData<List<LearningSession>>
}