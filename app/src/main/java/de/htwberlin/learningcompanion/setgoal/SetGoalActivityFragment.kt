package de.htwberlin.learningcompanion.setgoal

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import de.htwberlin.learningcompanion.R

/**
 * A placeholder fragment containing a simple view.
 */
class SetGoalActivityFragment : Fragment() {

    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_set_goal, container, false)
        return view
    }
}