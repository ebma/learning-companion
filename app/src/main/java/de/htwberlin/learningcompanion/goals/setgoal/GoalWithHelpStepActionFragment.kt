package de.htwberlin.learningcompanion.goals.setgoal

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.htwberlin.learningcompanion.R
import org.jetbrains.anko.support.v4.runOnUiThread

class GoalWithHelpStepActionFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var actionEditText: TextInputEditText
    private lateinit var actionInputLayout: TextInputLayout
    private lateinit var doneButton: ImageButton

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_with_help_step_action, container, false)
        findViews()

        addDoneButtonClickListener()
        return rootView
    }

    private fun findViews() {

        actionEditText = rootView.findViewById(R.id.et_action)
        actionInputLayout = rootView.findViewById(R.id.til_action)
        doneButton = rootView.findViewById<ImageButton>(R.id.btn_done)
    }

    private fun addDoneButtonClickListener() {
        doneButton.setOnClickListener { navigateToStepFieldFragmentWithValues() }

        doneButton.setOnClickListener {
            navigateToStepFieldFragmentWithValues()
        }
    }

    private fun navigateToStepFieldFragmentWithValues() {

        val bundle = Bundle()
        fillBundleWithArguments(bundle)

        if(bundle.size() == 1)
            Navigation.findNavController(rootView).navigate(R.id.action_goalWithHelpStepActionFragment_to_goalWithHelpStepFieldFragment, bundle)
    }

    private fun fillBundleWithArguments(bundle: Bundle) {
        actionEditText.text.toString().let {
            if (it.isEmpty()) {
                tintTextInputLayout(actionInputLayout, true)
            } else {
                bundle.putString("action", it)
                tintTextInputLayout(actionInputLayout, false)
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
