package de.htwberlin.learningcompanion.goaloverview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.model.Goal
import de.htwberlin.learningcompanion.setgoal.GoalNavHostFragment
import de.htwberlin.learningcompanion.ui.GoalListAdapter
import de.htwberlin.learningcompanion.util.setActivityTitle
import kotlinx.android.synthetic.main.goal_overview_fragment.*
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
        rootView = inflater.inflate(R.layout.goal_overview_fragment, container, false)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setActivityTitle(getString(R.string.title_nav_menu_history))
//        (activity as MainActivity).supportActionBar?.title = getString(R.string.title_nav_menu_goal)


        viewModel = ViewModelProviders.of(this).get(GoalOverviewViewModel::class.java)
        viewModel.getGoals().observe(this, Observer<List<Goal>> { goals ->
            goalList.addAll(goals)
            viewAdapter.notifyDataSetChanged()
        })

        viewManager = LinearLayoutManager(context)
        viewAdapter = GoalListAdapter(goalList)

        recyclerView = rootView.findViewById<RecyclerView>(R.id.rv_goals).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager
            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
        btn_new_goal.onClick {
            navigateToSetGoalFragment()

        }


    }
    private fun navigateToSetGoalFragment() {
        val fragment = GoalNavHostFragment()
//        var fragment: Fragment? = null
//        R.id.nav_setgoal -> fragment = GoalNavHostFragment()

        activity!!.supportFragmentManager.beginTransaction().addToBackStack(
                "detailfragment").replace(R.id.content_main, fragment).commit()
    }

}
