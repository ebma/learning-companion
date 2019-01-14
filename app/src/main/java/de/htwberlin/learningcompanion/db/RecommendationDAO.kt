package de.htwberlin.learningcompanion.db

import androidx.room.Dao
import androidx.room.Query
import de.htwberlin.learningcompanion.model.Goal
import de.htwberlin.learningcompanion.model.Place

@Dao
interface RecommendationDAO {

    @Query("SELECT * FROM goals g, sessions s WHERE s.goal_id = g.id AND s.userRating > 70 ORDER BY s.userRating DESC")
    fun getGoalsByDescendingUserRating(): List<Goal>

    @Query("SELECT * FROM places p, sessions s WHERE s.goal_id = p.id GROUP BY p.id HAVING sum(s.userRating) > 0 ORDER BY sum(s.userRating) DESC")
    fun getPlacesByDescendingUserRating(): List<Place>

    @Query("SELECT * FROM goals g, sessions s WHERE s.goal_id = g.id ORDER BY s.userRating DESC")
    fun getBestTime(): List<Goal>

    @Query("SELECT durationInMin FROM goals g, sessions s WHERE s.goal_id = g.id AND s.userRating = ( SELECT max(userRating) FROM sessions)")
    fun getBestDuration(): Int

    @Query("SELECT * FROM goals g, sessions s WHERE s.goal_id = g.id ORDER BY s.userRating DESC")
    fun getBestBrightnessValue(): List<Goal>

    @Query("SELECT * FROM goals g, sessions s WHERE s.goal_id = g.id ORDER BY s.userRating DESC")
    fun getBestNoiseValue(): List<Goal>

}