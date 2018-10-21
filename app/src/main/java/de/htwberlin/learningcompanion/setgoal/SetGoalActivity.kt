package de.htwberlin.learningcompanion.setgoal

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import de.htwberlin.learningcompanion.R
import kotlinx.android.synthetic.main.activity_set_goal.*

class SetGoalActivity : FragmentActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_goal)
        setActionBar(toolbar)


        navController = Navigation.findNavController(this, R.id.navhost_set_goal)
        navController.navigate(R.id.action_setGoalActivityFragment_to_goalDecidePathFragment)
    }

}
