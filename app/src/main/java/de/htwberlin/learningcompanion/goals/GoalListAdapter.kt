package de.htwberlin.learningcompanion.goals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.model.Goal
import org.jetbrains.anko.sdk27.coroutines.onClick

class GoalListAdapter(private val goalDataSet: ArrayList<Goal>, val supportFragmentManager: FragmentManager) :
        RecyclerView.Adapter<GoalListAdapter.MyViewHolder>() {

    class MyViewHolder(val rootView: View, private val supportFragmentManager: FragmentManager) : RecyclerView.ViewHolder(rootView) {
        private lateinit var goal: Goal

        private val tvAction: TextView
        private val tvAmount: TextView
        private val tvMedium: TextView
        private val tvField: TextView
        private val tvDuration: TextView
        private val cbSetCurrent: CheckBox

        private val clGoalListItem: ConstraintLayout

        init {
            tvAction = rootView.findViewById(R.id.tv_action)
            tvAmount = rootView.findViewById(R.id.tv_amount)
            tvMedium = rootView.findViewById(R.id.tv_medium)
            tvField = rootView.findViewById(R.id.tv_field)
            tvDuration = rootView.findViewById(R.id.tv_duration)
            cbSetCurrent = rootView.findViewById(R.id.cb_set_current_goal)

            clGoalListItem = rootView.findViewById(R.id.cl_goal_list_item)
        }

        fun bindGoal(goal: Goal) {
            this.goal = goal
            tvAction.text = goal.action
            tvAmount.text = goal.amount
            tvMedium.text = goal.medium
            tvField.text = goal.field
            tvDuration.text = getGoalDurationText(goal)

            cbSetCurrent.isChecked = goal.currentGoal
            cbSetCurrent.onClick {
                GoalRepository.get(itemView.context).setGoalAsCurrentGoal(goal)
            }

            clGoalListItem.onClick {
                GoalRepository.get(itemView.context).setGoalAsCurrentGoal(goal)
            }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_list_item, parent, false) as View
        return MyViewHolder(view, supportFragmentManager)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val goal = goalDataSet[position]
        holder.bindGoal(goal)
    }

    override fun getItemCount() = goalDataSet.size

}