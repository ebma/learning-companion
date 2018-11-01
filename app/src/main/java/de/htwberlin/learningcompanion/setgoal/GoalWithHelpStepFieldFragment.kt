package de.htwberlin.learningcompanion.setgoal

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.htwberlin.learningcompanion.R
import org.jetbrains.anko.support.v4.runOnUiThread


class GoalWithHelpStepFieldFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var fieldEditText: TextInputEditText
    private lateinit var fieldInputLayout: TextInputLayout
    private lateinit var doneButton: Button

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_with_help_step_field, container, false)
        findViews()

        addDoneButtonClickListener()
        return rootView
    }

    private fun findViews() {

        fieldEditText = rootView.findViewById(R.id.et_field)
        fieldInputLayout = rootView.findViewById(R.id.til_field)
        doneButton = rootView.findViewById<Button>(R.id.btn_done)

    }

    private fun addDoneButtonClickListener() {
        doneButton.setOnClickListener {
            navigateToStepObjectFragmentWithValues()
        }
    }

    private fun navigateToStepObjectFragmentWithValues() {
//        var action = arguments?.getString("action")
//        val fieldEditText = rootView.findViewById<EditText>(R.id.et_field)

        val bundle = Bundle()
//        bundle.putString("action", action)
//        bundle.putString("field", fieldEditText.text.toString())
        fillBundleWithArguments(bundle)

        if(bundle.size() == 2)
            Navigation.findNavController(rootView).navigate(R.id.action_goalWithHelpStepFieldFragment_to_goalWithHelpStepObjectFragment, bundle)
    }

    private fun fillBundleWithArguments(bundle: Bundle) {
        var action = arguments?.getString("action")
//        val fieldEditText = rootView.findViewById<EditText>(R.id.et_field)

        bundle.putString("action", action)

        fieldEditText.text.toString().let {
            if (it.isEmpty()) {
                tintTextInputLayout(fieldInputLayout, true)
            } else {
                bundle.putString("field", it)
                tintTextInputLayout(fieldInputLayout, false)
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
