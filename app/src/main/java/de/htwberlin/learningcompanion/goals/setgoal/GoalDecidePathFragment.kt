package de.htwberlin.learningcompanion.goals.setgoal


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import de.htwberlin.learningcompanion.R

class GoalDecidePathFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_decide_path, container, false)
        addClickListeners()
        return rootView
    }

    private fun addClickListeners(){
        addNoButtonClickListener()
        addYesButtonClickListener()
    }

    private fun addNoButtonClickListener(){
        val noButton = rootView.findViewById<Button>(R.id.btn_no)
        noButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_goalDecidePathFragment_to_goalWithHelpInfoFragment))
    }


    private fun addYesButtonClickListener(){
        val yesButton = rootView.findViewById<Button>(R.id.btn_yes)
        yesButton.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_goalDecidePathFragment_to_goalNoHelpInfoFragment))
    }


}
