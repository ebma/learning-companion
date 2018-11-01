package de.htwberlin.learningcompanion.setgoal

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


class GoalWithHelpStepObjectFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var objectEditText: TextInputEditText
    private lateinit var objectInputLayout: TextInputLayout
    private lateinit var doneButton: Button

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_with_help_step_object, container, false)
        findViews()

        addDoneButtonClickListener()
        return rootView
    }

    private fun findViews() {

        objectEditText = rootView.findViewById(R.id.et_object)
        objectInputLayout = rootView.findViewById(R.id.til_object)
        doneButton = rootView.findViewById<Button>(R.id.btn_done)

    }

    private fun addDoneButtonClickListener() {
        doneButton.setOnClickListener {
            navigateToStepAmountFragmentWithValues()
        }
    }

    private fun navigateToStepAmountFragmentWithValues() {
//        val action = arguments?.getString("action")
//        val field = arguments?.getString("field")

//        val mediumEditText = rootView.findViewById<EditText>(R.id.et_object)

        val bundle = Bundle()
//        bundle.putString("action", action)
//        bundle.putString("field", field)
//        bundle.putString("medium", mediumEditText.text.toString())

        fillBundleWithArguments(bundle)

        if(bundle.size() == 3)
            Navigation.findNavController(rootView).navigate(R.id.action_goalWithHelpStepObjectFragment_to_goalWithHelpStepAmountFragment, bundle)
    }

    private fun fillBundleWithArguments(bundle: Bundle) {
        val action = arguments?.getString("action")
        val field = arguments?.getString("field")
//        val fieldEditText = rootView.findViewById<EditText>(R.id.et_field)

        bundle.putString("action", action)
        bundle.putString("field", field)

        objectEditText.text.toString().let {
            if (it.isEmpty()) {
                tintTextInputLayout(objectInputLayout, true)
            } else {
                bundle.putString("medium", it)
                tintTextInputLayout(objectInputLayout, false)
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
