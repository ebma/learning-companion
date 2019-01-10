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


class GoalWithHelpStepObjectFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var objectEditText: TextInputEditText
    private lateinit var objectInputLayout: TextInputLayout
    private lateinit var doneButton: ImageButton
    private lateinit var backButton: ImageButton

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_with_help_step_object, container, false)
        findViews()

        addDoneButtonClickListener()
        addBackButtonClickListener()

        getFromBundle()

        return rootView
    }

    private fun findViews() {

        objectEditText = rootView.findViewById(R.id.et_object)
        objectInputLayout = rootView.findViewById(R.id.til_object)
        doneButton = rootView.findViewById(R.id.btn_done)
        backButton = rootView.findViewById(R.id.btn_back)

    }

    private fun addDoneButtonClickListener() {
        doneButton.setOnClickListener {
            navigateToStepAmountFragmentWithValues()
        }
    }

    private fun addBackButtonClickListener() {
        backButton.setOnClickListener {
            navigateToStepFieldFragmentWithValues()
        }
    }

    private fun navigateToStepFieldFragmentWithValues() {
        val bundle = Bundle()
        fillBundleWithArguments(bundle)

        if(bundle.size() >= 2)
            Navigation.findNavController(rootView).navigate(R.id.action_goalWithHelpStepObjectFragment_back_to_goalWithHelpStepFieldFragment, bundle)
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

//        if(bundle.size() == 3)
            Navigation.findNavController(rootView).navigate(R.id.action_goalWithHelpStepObjectFragment_to_goalWithHelpStepAmountFragment, bundle)
    }

    private fun fillBundleWithArguments(bundle: Bundle) {
        val action = arguments?.getString("action")
        val field = arguments?.getString("field")
//        val medium = arguments?.getString("medium")
        val amount = arguments?.getString("amount")
        var duration = arguments?.getString("duration")
        var timestamp = arguments?.getString("timestamp")

        bundle.putString("action", action)
        bundle.putString("field", field)
//        bundle.putString("medium", medium)
        bundle.putString("amount", amount)
        bundle.putString("duration", duration)
        bundle.putString("timestamp", timestamp)

        objectEditText.text.toString().let {
            if (it.isEmpty()) {
                tintTextInputLayout(objectInputLayout, true)
            } else {
                bundle.putString("medium", it)
                tintTextInputLayout(objectInputLayout, false)
            }
        }
    }

    private fun getFromBundle() {
        var medium = arguments?.getString("medium")
        if(!medium.isNullOrEmpty()) {
            objectEditText.setText(medium)
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
