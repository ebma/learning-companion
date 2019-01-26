package de.htwberlin.learningcompanion.learning.session

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.learning.LightLevel
import de.htwberlin.learningcompanion.learning.NoiseLevel
import de.htwberlin.learningcompanion.model.Goal
import de.htwberlin.learningcompanion.model.LearningSession
import de.htwberlin.learningcompanion.model.Place
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.text.SimpleDateFormat
import java.util.*

val dateFormat: SimpleDateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())

class LearningSessionListAdapter(private val sessionDataSet: ArrayList<LearningSession>, val supportFragmentManager: FragmentManager) :
        RecyclerView.Adapter<LearningSessionListAdapter.MyViewHolder>() {

    class MyViewHolder(val rootView: View, private val supportFragmentManager: FragmentManager) : RecyclerView.ViewHolder(rootView) {
        private lateinit var session: LearningSession
        private lateinit var goal: Goal
        private lateinit var place: Place

        private val tvDate: TextView
        private val tvGoal: TextView
        private val tvPlace: TextView
        private val ivBrightness: ImageView
        private val ivNoise: ImageView
        private val ivUserrating: ImageView

        private val thumbUpDrawable: Drawable?
        private val thumbDownDrawable: Drawable?

        private val clSessionListItem: ConstraintLayout

        init {
            tvDate = rootView.findViewById(R.id.tv_date)
            tvGoal = rootView.findViewById(R.id.tv_goal)
            tvPlace = rootView.findViewById(R.id.tv_place)
            ivBrightness = rootView.findViewById(R.id.iv_brightness)
            ivNoise = rootView.findViewById(R.id.iv_noise)
            ivUserrating = rootView.findViewById(R.id.iv_rate)

            thumbUpDrawable = rootView.context.getDrawable(R.drawable.ic_thumb_up)
            thumbDownDrawable = rootView.context.getDrawable(R.drawable.ic_thumb_down)

            clSessionListItem = rootView.findViewById(R.id.cl_session_list_item)
        }

        fun bindSession(session: LearningSession) {
            this.session = session
            goal = GoalRepository.get(itemView.context).getGoalByID(session.goalID)
            place = PlaceRepository.get(itemView.context).getPlaceByID(session.placeID)

            tvDate.text = dateFormat.format(session.createdAt)
            tvGoal.text = goal.getGoalText()
            tvPlace.text = place.name
            ivBrightness.setColorFilter(getColorForLightLevel(session.brightnessRating))
            ivNoise.setColorFilter(getColorForNoiseLevel(session.noiseRating))
            ivUserrating.setColorFilter(getColorForUserrating(session.userRating))
            if (session.userRating >= 70) {
                ivUserrating.setImageDrawable(thumbUpDrawable)
            } else {
                ivUserrating.setImageDrawable(thumbDownDrawable)
            }
            clSessionListItem.onClick {
                navigateToViewSessionFragment()
            }
        }

        fun navigateToViewSessionFragment() {
            val bundle = Bundle()
            bundle.putLong("ID", session.id)

            val fragment = SessionFragment()
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().addToBackStack("detailfragment").replace(R.id.content_main, fragment).commit()
        }

        private fun getColorForNoiseLevel(noiseLevel: NoiseLevel): Int {
            return when (noiseLevel) {
                NoiseLevel.LOWEST -> ContextCompat.getColor(itemView.context, R.color.lightgreen)
                NoiseLevel.LOW -> ContextCompat.getColor(itemView.context, R.color.green)
                NoiseLevel.MEDIUM -> ContextCompat.getColor(itemView.context, R.color.orange)
                NoiseLevel.HIGH -> ContextCompat.getColor(itemView.context, R.color.lightred)
                NoiseLevel.HIGHEST -> ContextCompat.getColor(itemView.context, R.color.red)
            }
        }

        private fun getColorForLightLevel(lightLevel: LightLevel): Int {
            return when (lightLevel) {
                LightLevel.LOWEST -> ContextCompat.getColor(itemView.context, R.color.red)
                LightLevel.LOW -> ContextCompat.getColor(itemView.context, R.color.lightred)
                LightLevel.MEDIUM -> ContextCompat.getColor(itemView.context, R.color.orange)
                LightLevel.HIGH -> ContextCompat.getColor(itemView.context, R.color.green)
                LightLevel.HIGHEST -> ContextCompat.getColor(itemView.context, R.color.lightgreen)
            }
        }

        private fun getColorForUserrating(userrating: Int): Int {
            return when (userrating) {
                in 0..20 -> ContextCompat.getColor(itemView.context, R.color.red)
                in 21..40 -> ContextCompat.getColor(itemView.context, R.color.lightred)
                in 41..60 -> ContextCompat.getColor(itemView.context, R.color.orange)
                in 61..80 -> ContextCompat.getColor(itemView.context, R.color.green)
                in 81..100 -> ContextCompat.getColor(itemView.context, R.color.lightgreen)
                else -> ContextCompat.getColor(itemView.context, R.color.black)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.session_list_item, parent, false) as View
        return MyViewHolder(view, supportFragmentManager)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val session = sessionDataSet[position]
        holder.bindSession(session)
    }

    override fun getItemCount() = sessionDataSet.size

}