package de.htwberlin.learningcompanion.setgoal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import de.htwberlin.learningcompanion.R


class GoalWithHelpStepFieldFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_with_help_step_field, container, false)
        addDoneButtonClickListener()
        return rootView
    }

    private fun addDoneButtonClickListener() {
        val doneButton = rootView.findViewById<Button>(R.id.btn_done)
        doneButton.setOnClickListener { navigateToStepObjectFragmentWithValues() }
    }

    private fun navigateToStepObjectFragmentWithValues() {
        var action = arguments?.getString("action")
        val fieldEditText = rootView.findViewById<EditText>(R.id.et_field)

        val bundle = Bundle()
        bundle.putString("action", action)
        bundle.putString("discipline", fieldEditText.text.toString())

        Navigation.findNavController(rootView).navigate(R.id.action_goalWithHelpStepFieldFragment_to_goalWithHelpStepObjectFragment, bundle)
    }


}
