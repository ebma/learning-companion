package de.htwberlin.learningcompanion.setgoal


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.MainActivity
import de.htwberlin.learningcompanion.R

class GoalNavHostFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as MainActivity).supportActionBar?.title = "New goal"

        return inflater.inflate(R.layout.fragment_goal_nav_host, container, false)
    }


}
