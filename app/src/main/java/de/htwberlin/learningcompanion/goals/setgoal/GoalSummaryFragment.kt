package de.htwberlin.learningcompanion.goals.setgoal


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.goals.overview.GoalOverviewFragment
import de.htwberlin.learningcompanion.model.Goal

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
        val goal = Goal(action!!, amount!!, field!!, medium!!, duration?.toInt(), timestamp)

        goalTextTextView.text = goal.getGoalText()
    }

    private fun addNoButtonClickListener() {
        rootView.findViewById<Button>(R.id.btn_no).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_goalSummaryFragment_to_goalDecidePathFragment))

//        activity?.supportFragmentManager?.backStackEntryCount

    }

    private fun addYesButtonOnClickListener() {
        rootView.findViewById<Button>(R.id.btn_yes).setOnClickListener {
            val goal = createGoalFromArguments()
            saveGoalToDatabase(goal)
            navigateToGoalOverview()
        }
    }

    private fun createGoalFromArguments(): Goal {
        return if (duration != null) {
            Goal(action!!, amount!!, field!!, medium!!, duration?.toInt(), null)
        } else {
            Goal(action!!, amount!!, field!!, medium!!, null, timestamp!!)
        }
    }

    private fun saveGoalToDatabase(goal: Goal) {
        context?.let {
            goal.currentGoal = true
            GoalRepository.get(it).setNoGoalAsCurrentGoal()
            GoalRepository.get(it).insertGoal(goal)
        }
    }

    private fun navigateToGoalOverview() {
        // (activity as MainActivity).nav_view.setCheckedItem(R.id.nav_mainscreen)

        val fragment = GoalOverviewFragment()
        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.content_main, fragment)?.commit()
    }
}
