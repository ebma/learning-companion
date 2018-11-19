package de.htwberlin.learningcompanion.goals.setgoal

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.htwberlin.learningcompanion.R
import org.jetbrains.anko.support.v4.runOnUiThread


class GoalWithHelpStepAmountFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var amountEditText: TextInputEditText
    private lateinit var amountInputLayout: TextInputLayout
    private lateinit var doneButton: Button


//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_with_help_step_amount, container, false)
        findViews()

        addDoneButtonClickListener()
        return rootView
    }

    private fun findViews() {

        amountEditText = rootView.findViewById(R.id.et_amount)
        amountInputLayout = rootView.findViewById(R.id.til_amount)
        doneButton = rootView.findViewById<Button>(R.id.btn_done)

    }

    private fun addDoneButtonClickListener() {
        doneButton.setOnClickListener {
            navigateToStepDurationFragmentWithValues()
        }
    }

    private fun navigateToStepDurationFragmentWithValues() {
//        val action = arguments?.getString("action")
//        val field = arguments?.getString("field")
//        val medium = arguments?.getString("medium")

        val amountEditText = rootView.findViewById<EditText>(R.id.et_amount)

        val bundle = Bundle()
        fillBundleWithArguments(bundle)
//        bundle.putString("action", action)
//        bundle.putString("field", field)
//        bundle.putString("medium", medium)
//        bundle.putString("amount", amountEditText.text.toString())

        if(bundle.size() == 4)
            Navigation.findNavController(rootView).navigate(R.id.action_goalWithHelpStepAmountFragment_to_goalWithHelpStepDurationFragment, bundle)
    }

    private fun fillBundleWithArguments(bundle: Bundle) {
        val action = arguments?.getString("action")
        val field = arguments?.getString("field")
        val medium = arguments?.getString("medium")
//        val fieldEditText = rootView.findViewById<EditText>(R.id.et_field)

        bundle.putString("action", action)
        bundle.putString("field", field)
        bundle.putString("medium", medium)

        amountEditText.text.toString().let {
            if (it.isEmpty()) {
                tintTextInputLayout(amountInputLayout, true)
            } else {
                bundle.putString("amount", it)
                tintTextInputLayout(amountInputLayout, false)
            }
        }
    }

    private fun tintTextInputLayout(layout: TextInputLayout, errorTint: Boolean) {
        runOnUiThread {
            if (errorTint)
                layout.defaultHintTextColor = ColorStateList.valueOf(resources.getColor(android.R.color.holo_red_dark))
            else
                layout.defaultHintTextColor = ColorStateList.valueOf(resources.getColor(android.R.color.darker_gray))
        }
    }

}
