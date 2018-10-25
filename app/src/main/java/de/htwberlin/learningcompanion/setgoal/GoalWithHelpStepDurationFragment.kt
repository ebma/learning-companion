package de.htwberlin.learningcompanion.setgoal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import de.htwberlin.learningcompanion.R


class GoalWithHelpStepDurationFragment : Fragment() {

    private lateinit var rootView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_with_help_step_duration, container, false)
        addDoneButtonClickListener()
        return rootView
    }

    private fun addDoneButtonClickListener(){
        val doneButton = rootView.findViewById<Button>(R.id.btn_done)
        doneButton.setOnClickListener(Navigation.createNavigateOnClickListener(
                R.id.action_goalWithHelpStepDurationFragment_to_goalSummaryFragment, null))
    }

}
