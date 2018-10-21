package de.htwberlin.learningcompanion.setgoal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import de.htwberlin.learningcompanion.R

class GoalWithHelpStepActionFragment : Fragment() {

    private lateinit var rootView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addDoneButtonClickListener()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_with_help_step_action, container, false)

        addDoneButtonClickListener()
        return rootView
    }

    private fun addDoneButtonClickListener(){
        val doneButton = rootView.findViewById<Button>(R.id.btn_done)
        doneButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.goalWithHelpStepFieldFragment, null))
    }

}
