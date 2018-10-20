package de.htwberlin.learningcompanion.setgoal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import de.htwberlin.learningcompanion.R

class GoalWithHelpInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_goal_with_help_info, container, false)

        val constraintLayout = view.findViewById<ConstraintLayout>(R.id.cl_goal_with_help_info)
        constraintLayout.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.goalWithHelpStepActionFragment, null))
        return view
    }

}
