package de.htwberlin.learningcompanion.goals.setgoal

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import de.htwberlin.learningcompanion.R
import java.util.*


class GoalWithHelpStepDurationFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var forAmountEditText: EditText
    private lateinit var untilAmountEditText: EditText
    private lateinit var forRadioButton: RadioButton
    private lateinit var untilRadioButton: RadioButton
    private lateinit var doneButton: ImageButton
    private lateinit var backButton: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_with_help_step_duration, container, false)

        findViews()
        addDoneButtonClickListener()
        addBackButtonClickListener()
        addRadioButtonClickListeners()
        addTimePickerDialogToUntilAmountEditText()

        getFromBundle()

        return rootView
    }

    private fun findViews() {
        forAmountEditText = rootView.findViewById(R.id.et_duration_for)
        untilAmountEditText = rootView.findViewById(R.id.et_duration_until)

        untilRadioButton = rootView.findViewById(R.id.rb_until_with_help)
        forRadioButton = rootView.findViewById(R.id.rb_for_with_help)

        doneButton = rootView.findViewById(R.id.btn_done)
        backButton = rootView.findViewById(R.id.btn_back)
    }

    private fun addRadioButtonClickListeners() {
        forRadioButton.setOnClickListener {
            forAmountEditText.isEnabled = true
            untilAmountEditText.isEnabled = false
        }

        untilRadioButton.setOnClickListener {
            forAmountEditText.isEnabled = false
            untilAmountEditText.isEnabled = true
        }
    }

    private fun addTimePickerDialogToUntilAmountEditText() {
        untilAmountEditText.isFocusable = false // this will prevent keyboard from showing
        untilAmountEditText.clearFocus()

        untilAmountEditText.setOnClickListener {
            showTimePickerDialogForTextView(it as TextView)
        }
    }

    private fun showTimePickerDialogForTextView(view: TextView) {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
            view.text = createTimeStringFromValues(selectedHour, selectedMinute)
        }, hour, minute, true)
        mTimePicker.setTitle(getString(R.string.select_time))
        mTimePicker.show()
    }

    private fun createTimeStringFromValues(hour: Int, minute: Int): String {
        var minuteString = minute.toString()
        var hourString = hour.toString()

        if (minute < 10) {
            minuteString = "0$minuteString"
        }

        if (hour < 10) {
            hourString = "0$hourString"
        }

        return "$hourString:$minuteString"
    }

    private fun addDoneButtonClickListener() {
        doneButton.setOnClickListener { navigateToSummaryFragmentWithValues() }
    }

    private fun addBackButtonClickListener() {
        backButton.setOnClickListener {
            navigateToStepAmountFragmentWithValues()
        }
    }

    private fun navigateToStepAmountFragmentWithValues() {
        val bundle = Bundle()
        fillBundleWithArguments(bundle)

        if(bundle.size() >= 4)
            Navigation.findNavController(rootView).navigate(R.id.action_goalWithHelpStepObjectFragment_back_to_goalWithHelpStepAmountFragment, bundle)
    }

    private fun navigateToSummaryFragmentWithValues() {
//        val action = arguments?.getString("action")
//        val field = arguments?.getString("field")
//        val medium = arguments?.getString("medium")
//        val amount = arguments?.getString("amount")

//        val durationForEditText = rootView.findViewById<EditText>(R.id.et_duration_for)
//        val durationUntilEditText = rootView.findViewById<EditText>(R.id.et_duration_until)

        val bundle = Bundle()
        fillBundleWithArguments(bundle)
//        bundle.putString("action", action)
//        bundle.putString("field", field)
//        bundle.putString("medium", medium)
//        bundle.putString("amount", amount)
        /*if (durationForEditText.text.toString().length > 0)
            bundle.putString("timestamp", durationForEditText.text.toString())
        else
            bundle.putString("timestamp", durationUntilEditText.text.toString()) */

//        if (untilRadioButton.isChecked)
//            bundle.putString("timestamp", untilAmountEditText.text.toString())
//        else
//            bundle.putString("timestamp", forAmountEditText.text.toString())

//        if(bundle.size() == 6)
            Navigation.findNavController(rootView).navigate(R.id.action_goalWithHelpStepDurationFragment_to_goalSummaryFragment, bundle)
    }

    private fun getFromBundle() {
        var duration = arguments?.getString("duration")
        var timestamp = arguments?.getString("timestamp")

        if(!duration.isNullOrEmpty()) {
            forRadioButton.isEnabled = true
            forAmountEditText.setText(duration)
        } else if (!timestamp.isNullOrEmpty()) {
            untilRadioButton.isEnabled = true
            untilAmountEditText.setText(timestamp)
        }
    }

    private fun fillBundleWithArguments(bundle: Bundle) {
        val action = arguments?.getString("action")
        val field = arguments?.getString("field")
        val medium = arguments?.getString("medium")
        val amount = arguments?.getString("amount")
//        var duration = arguments?.getString("duration")
//        var timestamp = arguments?.getString("timestamp")

        bundle.putString("action", action)
        bundle.putString("field", field)
        bundle.putString("medium", medium)
        bundle.putString("amount", amount)
//        bundle.putString("duration", duration)
//        bundle.putString("timestamp", timestamp)

        val withHelp = true
        bundle.putBoolean("withHelp", withHelp)

        if (untilRadioButton.isChecked) {
            untilAmountEditText.text.toString().let {
                if (it.isEmpty()) {
                    // TODO something?
                } else
                    bundle.putString("timestamp", it)
            }
        } else {
            forAmountEditText.text.toString().let {
                if (it.isEmpty()) {
                    // TODO something?
                } else
                    bundle.putString("duration", it)
            }
        }
    }

//    private fun tintTextInputLayout(layout: TextInputLayout, errorTint: Boolean) {
//        runOnUiThread {
//            if (errorTint)
//                layout.defaultHintTextColor = ColorStateList.valueOf(resources.getColor(android.R.color.holo_red_dark))
//            else
//                layout.defaultHintTextColor = ColorStateList.valueOf(resources.getColor(android.R.color.darker_gray))
//        }
//    }

}
