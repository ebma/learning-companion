package de.htwberlin.learningcompanion.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.goaloverview.details.MyGoalFragment
import de.htwberlin.learningcompanion.model.Goal
import org.jetbrains.anko.sdk27.coroutines.onClick

class GoalListAdapter(private val goalDataSet: ArrayList<Goal>, val supportFragmentManager: FragmentManager) :
        RecyclerView.Adapter<GoalListAdapter.MyViewHolder>() {
// class GoalListAdapter(private val goalDataSet: ArrayList<Goal>) : RecyclerView.Adapter<GoalListAdapter.MyViewHolder>() {

    /*class MyViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalListAdapter.MyViewHolder {
        // create a new rootView
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_list_item, parent, false) as View
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.rootView.findViewById<TextView>(R.id.tv_goal).text = getGoalText(goalDataSet[position])

        holder.rootView.findViewById<TextView>(R.id.tv_goal_duration).text = getGoalDurationText(goalDataSet[position])

    }

    private fun getGoalText(goal: Goal): String {
        return goal.action.plus(goal.field).plus(goal.medium).plus(goal.amount)
    }

    private fun getGoalDurationText(goal: Goal): String {
        return if (goal.durationInMin != null) {
            "${goal.durationInMin} minutes"
        } else {
            "${goal.untilTimeStamp}"
        }
    }

    override fun getItemCount() = goalDataSet.size*/

    class MyViewHolder(val rootView: View, private val supportFragmentManager: FragmentManager) : RecyclerView.ViewHolder(rootView) {
        private lateinit var goal: Goal
        private val tvGoal: TextView
        private val tvDuration: TextView

        private val btnEdit: ImageButton
        private val cbSetCurrent: CheckBox

        init {
            tvGoal = rootView.findViewById(R.id.tv_goal)
            tvDuration = rootView.findViewById(R.id.tv_goal_duration)

            btnEdit = rootView.findViewById(R.id.btn_edit)
            btnEdit.onClick { navigateToGoalDetailFragment() }
            cbSetCurrent = rootView.findViewById(R.id.cb_set_current_goal)
        }
        fun bindGoal(goal: Goal) {
            this.goal = goal
            tvGoal.text = getGoalText(goal)
            tvDuration.text = getGoalDurationText(goal)

            cbSetCurrent.isChecked = goal.currentGoal

            cbSetCurrent.onClick {
                GoalRepository.get(itemView.context).setGoalAsCurrentGoal(goal)
            }

        }

        private fun navigateToGoalDetailFragment() {
            val fragment = MyGoalFragment()
            val bundle = Bundle()
            bundle.putLong("ID", goal.id)
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().addToBackStack("detailfragment").replace(R.id.content_main, fragment).commit()
        }

        private fun getGoalText(goal: Goal): String {
            return "${goal.action}, ${goal.field}, ${goal.medium}, ${goal.amount}"
        }

        private fun getGoalDurationText(goal: Goal): String {
            return if (goal.durationInMin != null) {
                "${goal.durationInMin} minutes"
            } else {
                "${goal.untilTimeStamp}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_list_item, parent, false) as View
        return MyViewHolder(view, supportFragmentManager)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val goal = goalDataSet[position]
        holder.bindGoal(goal)
    }

    /*private fun getGoalText(goal: Goal): String {
        return "${goal.action}, ${goal.field}, ${goal.medium}, ${goal.amount}"
    }

    private fun getGoalDurationText(goal: Goal): String {
        return if (goal.durationInMin != null) {
            "${goal.durationInMin} minutes"
        } else {
            "${goal.untilTimeStamp}"
        }
    }*/

    override fun getItemCount() = goalDataSet.size

}