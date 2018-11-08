package de.htwberlin.learningcompanion.setgoal


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import de.htwberlin.learningcompanion.MainActivity
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.AppDatabase
import de.htwberlin.learningcompanion.mainscreen.MainScreenFragment
import de.htwberlin.learningcompanion.model.Goal
import kotlinx.android.synthetic.main.activity_main.*

class GoalSummaryFragment : Fragment() {

    private var action: String? = null
    private var amount: String? = null
    private var field: String? = null
    private var medium: String? = null
    private var duration: String? = null
    private var timestamp: String? = null


    private lateinit var rootView: View
    private lateinit var goalTextTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_summary, container, false)

        initVariablesFromArguments()
        findViews()
        setGoalTextInTextView()

        addYesButtonOnClickListener()
        addNoButtonClickListener()
        return rootView
    }

    private fun findViews() {
        goalTextTextView = rootView.findViewById(R.id.tv_goal_text)
    }

    private fun initVariablesFromArguments() {
        amount = arguments?.getString("amount")
        action = arguments?.getString("action")
        field = arguments?.getString("field")
        medium = arguments?.getString("medium")

        duration = arguments?.getString("duration")
        timestamp = arguments?.getString("timestamp")
    }

    private fun setGoalTextInTextView() {
        val goalText = "${action} ${field} ${medium} ${amount} for/until ${timestamp ?: duration}"

        goalTextTextView.text = goalText
    }

    private fun addNoButtonClickListener() {
        rootView.findViewById<Button>(R.id.btn_no).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_goalSummaryFragment_to_goalDecidePathFragment))
    }

    private fun addYesButtonOnClickListener() {
        rootView.findViewById<Button>(R.id.btn_yes).setOnClickListener {
            val goal = createGoalFromArguments()
            saveGoalToDatabase(goal)
            navigateToMainScreen()
        }
    }

    private fun createGoalFromArguments(): Goal {
        var goal: Goal
        goal = try {
            val durationInMin = duration?.toInt()
            Goal(action!!, amount!!, field!!, medium!!, durationInMin, null)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            Goal(action!!, amount!!, field!!, medium!!, null, timestamp!!)
        }

        return goal
    }

    private fun saveGoalToDatabase(goal: Goal) {
        context?.let { AppDatabase.get(it).goalDao().insertGoal(goal) }
    }

    private fun navigateToMainScreen() {
        (activity as MainActivity).nav_view.setCheckedItem(R.id.nav_mainscreen)

        val fragment = MainScreenFragment()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.content_main, fragment)?.commit()
    }
}
