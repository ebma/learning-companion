package de.htwberlin.learningcompanion.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.model.Goal

class GoalListAdapter(private val goalDataSet: ArrayList<Goal>) : RecyclerView.Adapter<GoalListAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one rootView per item, and
    // you provide access to all the views for a data item in a rootView holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView)


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

    override fun getItemCount() = goalDataSet.size

}