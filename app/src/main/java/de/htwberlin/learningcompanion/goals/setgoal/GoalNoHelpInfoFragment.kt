package de.htwberlin.learningcompanion.goals.setgoal


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import de.htwberlin.learningcompanion.R


class GoalNoHelpInfoFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_no_help_info, container, false)
        addLayoutClickListener()
        return rootView
    }

    private fun addLayoutClickListener() {
        val constraintLayout = rootView.findViewById<ConstraintLayout>(R.id.cl_goal_no_help_info)
        constraintLayout.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_goalNoHelpInfoFragment_to_goalNoHelpUserInputFragment, null))
    }
}
