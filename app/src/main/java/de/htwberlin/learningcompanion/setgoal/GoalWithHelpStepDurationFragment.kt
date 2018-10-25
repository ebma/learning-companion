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


class GoalWithHelpStepDurationFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_with_help_step_duration, container, false)
        addDoneButtonClickListener()
        return rootView
    }

    private fun addDoneButtonClickListener() {
        val doneButton = rootView.findViewById<Button>(R.id.btn_done)
        doneButton.setOnClickListener { navigateToSummaryFragmentWithValues() }
    }

    private fun navigateToSummaryFragmentWithValues() {
        val action = arguments?.getString("action")
        val field = arguments?.getString("field")
        val medium = arguments?.getString("medium")
        val amount = arguments?.getString("amount")

        val durationForEditText = rootView.findViewById<EditText>(R.id.et_duration_for)
        val durationUntilEditText = rootView.findViewById<EditText>(R.id.et_duration_until)

        val bundle = Bundle()
        bundle.putString("action", action)
        bundle.putString("field", field)
        bundle.putString("medium", medium)
        bundle.putString("amount", amount)
        if (durationForEditText.text.toString().length > 0)
            bundle.putString("timestamp", durationForEditText.text.toString())
        else
            bundle.putString("timestamp", durationUntilEditText.text.toString())

        Navigation.findNavController(rootView).navigate(R.id.action_goalWithHelpStepDurationFragment_to_goalSummaryFragment, bundle)
    }

}
