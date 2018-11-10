package de.htwberlin.learningcompanion.setgoal


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.util.setActivityTitle

class GoalNavHostFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setActivityTitle("New goal")

        return inflater.inflate(R.layout.fragment_goal_nav_host, container, false)
    }


}
