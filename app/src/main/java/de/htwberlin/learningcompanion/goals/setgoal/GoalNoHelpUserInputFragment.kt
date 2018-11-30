package de.htwberlin.learningcompanion.goals.setgoal


import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.AppDatabase
import de.htwberlin.learningcompanion.model.Goal
import de.htwberlin.learningcompanion.util.setActivityTitle
import org.jetbrains.anko.support.v4.runOnUiThread

import java.util.*


class GoalNoHelpUserInputFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var forAmountEditText: EditText
    private lateinit var untilAmountEditText: EditText
    private lateinit var forRadioButton: RadioButton
    private lateinit var untilRadioButton: RadioButton
    private lateinit var doneButton: Button

    private lateinit var actionEditText: TextInputEditText
    private lateinit var fieldEditText: TextInputEditText
    private lateinit var amountEditText: TextInputEditText
    private lateinit var mediumEditText: TextInputEditText


    private lateinit var actionInputLayout: TextInputLayout
    private lateinit var fieldInputLayout: TextInputLayout
    private lateinit var amountInputLayout: TextInputLayout
    private lateinit var mediumInputLayout: TextInputLayout

    private var editMode = false
    private var goal: Goal? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_no_help_user_input, container, false)

        findViews()
        addDoneButtonClickListener()
        addRadioButtonClickListeners()
        addTimePickerDialogToUntilAmountEditText()

        checkForEditableGoal()

        return rootView
    }

    private fun checkForEditableGoal() {
        if (arguments != null) {
            val id = arguments!!.getLong("ID")
            context?.let {
                goal = AppDatabase.get(it).goalDao().getGoalByID(id)
            }
            if (goal != null) {
                initLayoutWithGoal(goal!!)
            }

            setActivityTitle("Edit goal")
            editMode = true

        } else {
            setActivityTitle("New Place")
            editMode = false
        }
    }

    private fun initLayoutWithGoal(goal: Goal) {
        actionEditText.setText(goal.action)
        fieldEditText.setText(goal.field)
        amountEditText.setText(goal.amount)
        mediumEditText.setText(goal.medium)

//        if(goal.durationInMin != null) {
//            forAmountEditText.setText(goal.durationInMin!!)
//        }  else {
//            untilAmountEditText.setText(goal.untilTimeStamp)
//        }

    }

    private fun findViews() {
        forAmountEditText = rootView.findViewById(R.id.et_for_amount)
        untilAmountEditText = rootView.findViewById(R.id.et_until_amount)

        untilRadioButton = rootView.findViewById(R.id.rb_until)
        forRadioButton = rootView.findViewById(R.id.rb_for)

        doneButton = rootView.findViewById(R.id.btn_done)

        actionEditText = rootView.findViewById(R.id.et_action)
        fieldEditText = rootView.findViewById(R.id.et_field)
        amountEditText = rootView.findViewById(R.id.et_amount)
        mediumEditText = rootView.findViewById(R.id.et_medium)

        actionInputLayout = rootView.findViewById(R.id.til_action)
        fieldInputLayout = rootView.findViewById(R.id.til_field)
        amountInputLayout = rootView.findViewById(R.id.til_amount)
        mediumInputLayout = rootView.findViewById(R.id.til_medium)
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
        doneButton.setOnClickListener {
            navigateToSummaryFragmentWithValues()
        }
    }

    private fun navigateToSummaryFragmentWithValues() {
        val bundle = Bundle()

        fillBundleWithArguments(bundle)

        if (bundle.size() == 5)
            Navigation.findNavController(rootView).navigate(R.id.action_goalNoHelpUserInputFragment_to_goalSummaryFragment, bundle)
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

        fieldEditText.text.toString().let {
            if (it.isEmpty()) {
                tintTextInputLayout(fieldInputLayout, true)
            } else {
                bundle.putString("field", it)
                tintTextInputLayout(fieldInputLayout, false)
            }
        }

        mediumEditText.text.toString().let {
            if (it.isEmpty()) {
                tintTextInputLayout(mediumInputLayout, true)
            } else {
                bundle.putString("medium", it)
                tintTextInputLayout(mediumInputLayout, false)
            }
        }

        amountEditText.text.toString().let {
            if (it.isEmpty()) {
                tintTextInputLayout(amountInputLayout, true)
            } else {
                bundle.putString("amount", it)
                tintTextInputLayout(amountInputLayout, false)
            }
        }

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

    private fun tintTextInputLayout(layout: TextInputLayout, errorTint: Boolean) {
        runOnUiThread {
            if (errorTint)
                layout.defaultHintTextColor = ColorStateList.valueOf(resources.getColor(android.R.color.holo_red_dark))
            else
                layout.defaultHintTextColor = ColorStateList.valueOf(resources.getColor(android.R.color.darker_gray))
        }
    }

}
