package de.htwberlin.learningcompanion.setgoal


import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import de.htwberlin.learningcompanion.R
import java.util.*


class GoalNoHelpUserInputFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var forAmountEditText: EditText
    private lateinit var untilAmountEditText: EditText
    private lateinit var forRadioButton: RadioButton
    private lateinit var untilRadioButton: RadioButton
    private lateinit var doneButton: Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_no_help_user_input, container, false)

        findViews()
        addDoneButtonClickListener()
        addRadioButtonClickListeners()
        addTimePickerDialogToUntilAmountEditText()

        return rootView
    }

    private fun findViews() {
        forAmountEditText = rootView.findViewById(R.id.et_for_amount)
        untilAmountEditText = rootView.findViewById(R.id.et_until_amount)

        untilRadioButton = rootView.findViewById(R.id.rb_until)
        forRadioButton = rootView.findViewById(R.id.rb_for)

        doneButton = rootView.findViewById(R.id.btn_done)
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

    private fun addDoneButtonClickListener() {
        doneButton.setOnClickListener { navigateToSummaryFragmentWithValues() }
    }

    private fun navigateToSummaryFragmentWithValues() {
        val actionEditText = rootView.findViewById<TextInputEditText>(R.id.et_action)
        val fieldEditText = rootView.findViewById<TextInputEditText>(R.id.et_discipline)
        val amountEditText = rootView.findViewById<TextInputEditText>(R.id.et_amount)
        val mediumEditText = rootView.findViewById<TextInputEditText>(R.id.et_medium)

        val bundle = Bundle()
        bundle.putString("action", actionEditText.text.toString())
        bundle.putString("field", fieldEditText.text.toString())
        bundle.putString("medium", mediumEditText.text.toString())
        bundle.putString("amount", amountEditText.text.toString())
        if (untilRadioButton.isSelected)
            bundle.putString("timestamp", untilAmountEditText.text.toString())
        else
            bundle.putString("timestamp", forAmountEditText.text.toString())

        Navigation.findNavController(rootView).navigate(R.id.action_goalNoHelpUserInputFragment_to_goalSummaryFragment, bundle)
    }

}
