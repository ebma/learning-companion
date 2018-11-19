package de.htwberlin.learningcompanion.learning.evaluation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.util.setActivityTitle

class EvaluationNavHostFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setActivityTitle("Evaluate Learning")

        return inflater.inflate(R.layout.fragment_evaluation_nav_host, container, false)
    }


}
