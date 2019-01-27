package de.htwberlin.learningcompanion.goals.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.buddy.BuddyFaceHolder
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.goals.GoalListAdapter
import de.htwberlin.learningcompanion.goals.setgoal.GoalNavHostFragment
import de.htwberlin.learningcompanion.goals.setgoal.GoalNoHelpUserInputFragment
import de.htwberlin.learningcompanion.mainscreen.MainScreenFragment
import de.htwberlin.learningcompanion.model.Goal
import de.htwberlin.learningcompanion.util.setActivityTitle
import kotlinx.android.synthetic.main.fragment_goal_overview.*
import org.jetbrains.anko.noButton
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.yesButton


class GoalOverviewFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: GoalListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var viewModel: GoalOverviewViewModel

    private lateinit var rootView: View
    private val goalList = ArrayList<Goal>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_goal_overview, container, false)

        updateHeaderLayout()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setActivityTitle(getString(R.string.title_nav_menu_goal))

        viewModel = ViewModelProviders.of(this).get(GoalOverviewViewModel::class.java)
        viewModel.getGoals().observe(this, Observer<List<Goal>> { goals ->
            goalList.clear()
            goalList.addAll(goals)
            viewAdapter.notifyDataSetChanged()
            updateHeaderLayout()
        })

        viewManager = LinearLayoutManager(context)
        viewAdapter = GoalListAdapter(goalList, activity!!.supportFragmentManager)

        recyclerView = rootView.findViewById<RecyclerView>(R.id.rv_goals).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        btn_new_goal.onClick {
            navigateToSetGoalFragment()
        }

        btn_goal_edit.onClick {
            navigateToEditGoal()
        }

        btn_delete_goal.onClick {
            openDeleteDialog()
        }
    }

    private fun openDeleteDialog() {
        alert("Do you REALLY want to delete the goal?", "Delete goal") {
            yesButton {
                deleteGoal()
                updateHeaderLayout()
            }
            noButton {
                toast("Good :)")
            }
        }.show()
    }

    private fun deleteGoal() {
        val currentGoal = GoalRepository.get(context!!).getCurrentGoal()
        if (currentGoal != null) {
            GoalRepository.get(context!!).deleteGoal(currentGoal)
            GoalRepository.get(context!!).setNoGoalAsCurrentGoal()
        }
    }

    private fun updateHeaderLayout() {
        rootView.findViewById<ImageView>(R.id.iv_charlie).setImageDrawable(BuddyFaceHolder.get(context!!).getDefaultFace())

        val goalRepository = GoalRepository.get(context!!)
        val textView = rootView.findViewById<TextView>(R.id.tv_charlie_info)

        if (goalRepository.goalsList?.isEmpty() == true) {
            textView.text = "You don't have added any goals yet. \nYou can add new goals by clicking on the \"Add\"-Button."
        } else {
            val currentGoal = goalRepository.getCurrentGoal()
            if (currentGoal != null) {
                textView.text = "Your currently selected goal is: \n${currentGoal.getGoalText()}"
            } else {
                textView.text = "You do not have a goal selected. \nYou can select one below or create a completely new goal."
            }
        }
    }

    private fun navigateToSetGoalFragment() {
        val fragment = GoalNavHostFragment()
        activity!!.supportFragmentManager.beginTransaction().addToBackStack("detailfragment").replace(R.id.content_main, fragment).commit()
    }
    private fun navigateToCharlie() {
        val fragment = MainScreenFragment()
        activity!!.supportFragmentManager.beginTransaction().addToBackStack("detailfragment").replace(R.id.content_main, fragment).commit()
    }

    private fun navigateToEditGoal() {
        val fragment = GoalNoHelpUserInputFragment()
        val bundle = Bundle()
        val currentGoal = GoalRepository.get(context!!).getCurrentGoal()

        if (currentGoal != null) {
            bundle.putLong("ID", currentGoal!!.id)
            fragment.arguments = bundle
            activity!!.supportFragmentManager.beginTransaction().addToBackStack("detailfragment").replace(R.id.content_main, fragment).commit()
        }
    }

}
