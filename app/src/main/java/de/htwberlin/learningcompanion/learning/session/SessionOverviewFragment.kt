package de.htwberlin.learningcompanion.learning.session

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
import de.htwberlin.learningcompanion.buddy.BuddyFaceHolder
import de.htwberlin.learningcompanion.db.LearningSessionRepository
import de.htwberlin.learningcompanion.model.LearningSession
import de.htwberlin.learningcompanion.util.setActivityTitle
import kotlinx.android.synthetic.main.fragment_session_overview.*

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


        setBuddyInfoText()
        setBuddyImage()
    }

    private fun setBuddyImage() {
        iv_charlie.setImageDrawable(BuddyFaceHolder.get(context!!).getDefaultFace())
    }

    private fun setBuddyInfoText() {
        if (LearningSessionRepository.get(context!!).sessionsList != null &&
                LearningSessionRepository.get(context!!).sessionsList!!.isNotEmpty()) {
            tv_charlie_info.text = "This is the overview of your learning history. \nYou can tap each entry to see the details."
        } else {
            tv_charlie_info.text = "This is the overview of your learning history. \nThere are no sessions to review yet."
        }
    }
}
