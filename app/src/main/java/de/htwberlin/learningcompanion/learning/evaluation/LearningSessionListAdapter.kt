package de.htwberlin.learningcompanion.learning.evaluation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.db.PlaceRepository
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

        private val clSessionListItem: ConstraintLayout

        init {
            tvDate = rootView.findViewById(R.id.tv_date)
            tvGoal = rootView.findViewById(R.id.tv_goal)
            tvPlace = rootView.findViewById(R.id.tv_place)
            ivBrightness = rootView.findViewById(R.id.iv_brightness)
            ivNoise = rootView.findViewById(R.id.iv_noise)
            ivUserrating = rootView.findViewById(R.id.iv_rate)

            clSessionListItem = rootView.findViewById(R.id.cl_session_list_item)
        }

        fun bindSession(session: LearningSession) {
            this.session = session
            goal = GoalRepository.get(itemView.context).getGoalByID(session.goalID)
            place = PlaceRepository.get(itemView.context).getPlaceByID(session.placeID)

            tvDate.text = dateFormat.format(session.createdAt)
            tvGoal.text = goal.getGoalText()
            tvPlace.text = place.name
//            ivBrightness.colorFilter =
//            ivNoise.colorFilter =
//            ivUserrating.colorFilter =

            clSessionListItem.onClick {
                navigateToViewSessionFragment()
            }
        }

        fun navigateToViewSessionFragment() {

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