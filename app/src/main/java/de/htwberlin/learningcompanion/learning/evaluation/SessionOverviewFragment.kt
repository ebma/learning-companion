package de.htwberlin.learningcompanion.learning.evaluation

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
import de.htwberlin.learningcompanion.model.LearningSession
import de.htwberlin.learningcompanion.util.setActivityTitle

class SessionOverviewFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: LearningSessionListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var viewModel: SessionOverviewViewModel

    private lateinit var rootView: View
    private val sessionList = ArrayList<LearningSession>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_session_overview, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setActivityTitle("My learning history")

        viewModel = ViewModelProviders.of(this).get(SessionOverviewViewModel::class.java)
        viewModel.getLearningSessions().observe(this, Observer<List<LearningSession>> { sessions ->
            sessionList.clear()
            sessionList.addAll(sessions)
            viewAdapter.notifyDataSetChanged()
        })

        viewManager = LinearLayoutManager(context)
        viewAdapter = LearningSessionListAdapter(sessionList, activity!!.supportFragmentManager)

        recyclerView = rootView.findViewById<RecyclerView>(R.id.rv_sessions).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}
