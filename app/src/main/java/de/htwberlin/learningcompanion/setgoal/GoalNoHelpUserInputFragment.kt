package de.htwberlin.learningcompanion.setgoal


import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.R
import java.util.*


class GoalNoHelpUserInputFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var forAmountEditText: EditText
    private lateinit var untilAmountEditText: EditText
    private lateinit var forRadioButton: RadioButton
    private lateinit var untilRadioButton: RadioButton


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_no_help_user_input, container, false)

        findViews()
        addRadioButtonClickListeners()
        addTimePickerDialogToUntilAmountEditText()

        return rootView
    }

    private fun findViews(){
        forAmountEditText = rootView.findViewById(R.id.et_for_amount)
        untilAmountEditText = rootView.findViewById(R.id.et_until_amount)

        untilRadioButton = rootView.findViewById(R.id.rb_until)
        forRadioButton = rootView.findViewById(R.id.rb_for)
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

    private fun addTimePickerDialogToUntilAmountEditText(){
        untilAmountEditText.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                untilAmountEditText.setText(selectedHour.toString() + ":" + selectedMinute)
            }, hour, minute, true)
            mTimePicker.setTitle(getString(R.string.select_time))
            mTimePicker.show()
        }
    }

}
