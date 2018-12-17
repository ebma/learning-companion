package de.htwberlin.learningcompanion.goals.details

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.goals.overview.GoalOverviewFragment
import de.htwberlin.learningcompanion.model.Goal
import de.htwberlin.learningcompanion.util.setActivityTitle
import kotlinx.android.synthetic.main.fragment_goal_no_help_user_input.*
import java.util.*

class EditGoalFragment : Fragment() {

    private var goal: Goal? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_goal_no_help_user_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActivityTitle("Edit goal")

        getGoalIDFromArguments()
        initLayoutWithExistingValues()
        addRadioButtonClickListeners()
        addDoneButtonClickListener()
        addTimePickerDialogToUntilAmountEditText()

    }

    private fun getGoalIDFromArguments() {
        if (arguments != null) {
            val id = arguments!!.getLong("ID")
            context?.let {
                goal = GoalRepository.get(it).getGoalByID(id)
            }
        }
    }

    private fun initLayoutWithExistingValues() {
        et_field.setText(goal?.field)
        et_amount.setText(goal?.amount)
        et_action.setText(goal?.action)
        et_medium.setText(goal?.medium)

        et_for_amount.setText(goal?.durationInMin.toString())
        et_until_amount.setText(goal?.untilTimeStamp)
    }

    private fun addRadioButtonClickListeners() {
        rb_for.setOnClickListener {
            et_for_amount.isEnabled = true
            et_until_amount.isEnabled = false
        }

        rb_until.setOnClickListener {
            et_for_amount.isEnabled = false
            et_until_amount.isEnabled = true
        }
    }

    private fun addTimePickerDialogToUntilAmountEditText() {
        et_until_amount.isFocusable = false // this will prevent keyboard from showing
        et_until_amount.clearFocus()

        et_until_amount.setOnClickListener {
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
        btn_done.setOnClickListener {
            createNewGoalFromValues()
            navigateToGoalOverview()
        }
    }

    private fun createNewGoalFromValues() {
        var forAmount: String? = null
        var untilAmount: String? = null

        if (rb_for.isChecked) {
            forAmount = et_for_amount.text.toString()
        } else if (rb_until.isChecked) {
            untilAmount = et_until_amount.text.toString()
        }

        val field = et_field.text.toString()
        val amount = et_amount.text.toString()
        val action = et_action.text.toString()
        val medium = et_medium.text.toString()

        val newGoal = Goal(action, amount, field, medium, forAmount?.toInt(), untilAmount)

        GoalRepository.get(context!!).insertGoal(newGoal)
    }

    private fun navigateToGoalOverview() {
        val fragment = GoalOverviewFragment()
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit()
    }
}