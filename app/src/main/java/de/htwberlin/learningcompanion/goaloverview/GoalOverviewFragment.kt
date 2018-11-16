package de.htwberlin.learningcompanion.goaloverview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.GoalRepository
import de.htwberlin.learningcompanion.model.Goal
import de.htwberlin.learningcompanion.setgoal.GoalNavHostFragment
import de.htwberlin.learningcompanion.ui.GoalListAdapter
import de.htwberlin.learningcompanion.util.setActivityTitle
import kotlinx.android.synthetic.main.fragment_goal_overview.*
import org.jetbrains.anko.sdk27.coroutines.onClick


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
    }

    private fun updateHeaderLayout() {
        val currentGoal = GoalRepository.get(context!!).getCurrentGoal()
        if (currentGoal != null) {
            rootView.findViewById<TextView>(R.id.tv_current_goal_text).text = currentGoal.getGoalText()
        }
    }

    private fun navigateToSetGoalFragment() {
        val fragment = GoalNavHostFragment()

        activity!!.supportFragmentManager.beginTransaction().addToBackStack("detailfragment").replace(R.id.content_main, fragment).commit()
    }

}
