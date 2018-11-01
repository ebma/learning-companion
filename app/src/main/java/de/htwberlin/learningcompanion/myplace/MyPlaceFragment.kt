package de.htwberlin.learningcompanion.myplace


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.htwberlin.learningcompanion.MainActivity
import de.htwberlin.learningcompanion.R

class MyPlaceFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        (activity as MainActivity)?.supportActionBar?.setTitle("My place")

        return inflater.inflate(R.layout.fragment_my_place, container, false)
    }


}
