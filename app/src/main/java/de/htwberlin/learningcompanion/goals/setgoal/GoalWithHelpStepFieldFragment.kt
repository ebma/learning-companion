package de.htwberlin.learningcompanion.goals.setgoal

import android.content.res.ColorStateList
import android.media.Image
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


class GoalWithHelpStepFieldFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var fieldEditText: TextInputEditText
    private lateinit var fieldInputLayout: TextInputLayout
    private lateinit var doneButton: ImageButton
    private lateinit var backButton: ImageButton


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_with_help_step_field, container, false)
        findViews()

        addDoneButtonClickListener()
        addBackButtonClickListener()

        getFromBundle()

        return rootView
    }

    private fun findViews() {

        fieldEditText = rootView.findViewById(R.id.et_field)
        fieldInputLayout = rootView.findViewById(R.id.til_field)
        doneButton = rootView.findViewById(R.id.btn_done)
        backButton = rootView.findViewById(R.id.btn_back)

    }

    private fun getFromBundle() {
        var field = arguments?.getString("field")
        if(!field.isNullOrEmpty()) {
            fieldEditText.setText(field)
        }
    }

    private fun addDoneButtonClickListener() {
        doneButton.setOnClickListener {
            navigateToStepObjectFragmentWithValues()
        }
    }

    private fun addBackButtonClickListener() {
        backButton.setOnClickListener {
            navigateToStepActionFragmentWithValues()
        }
    }

    private fun navigateToStepActionFragmentWithValues() {
        val bundle = Bundle()
        fillBundleWithArguments(bundle)

        if(bundle.size() >= 1)
            Navigation.findNavController(rootView).navigate(R.id.action_goalWithHelpStepFieldFragment_back_to_goalWithHelpStepActionFragment, bundle)
    }

    private fun navigateToStepObjectFragmentWithValues() {
        val bundle = Bundle()
        fillBundleWithArguments(bundle)

//        if(bundle.size() == 2)
            Navigation.findNavController(rootView).navigate(R.id.action_goalWithHelpStepFieldFragment_to_goalWithHelpStepObjectFragment, bundle)
    }

    private fun fillBundleWithArguments(bundle: Bundle) {
        var action = arguments?.getString("action")
//        val field = arguments?.getString("field")
        val medium = arguments?.getString("medium")
        val amount = arguments?.getString("amount")
        var duration = arguments?.getString("duration")
        var timestamp = arguments?.getString("timestamp")

        bundle.putString("action", action)
//        bundle.putString("field", field)
        bundle.putString("medium", medium)
        bundle.putString("amount", amount)
        bundle.putString("duration", duration)
        bundle.putString("timestamp", timestamp)

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
