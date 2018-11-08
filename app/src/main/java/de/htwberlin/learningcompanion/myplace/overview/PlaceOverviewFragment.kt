package de.htwberlin.learningcompanion.myplace.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.htwberlin.learningcompanion.MainActivity
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.model.Place
import de.htwberlin.learningcompanion.ui.PlaceListAdapter

class PlaceOverviewFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: PlaceListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var viewModel: PlaceOverviewViewModel

    private lateinit var rootView: View
    private val placeList = ArrayList<Place>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.place_overview_fragment, container, false)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as MainActivity).supportActionBar?.title = getString(R.string.title_nav_menu_history)


        viewModel = ViewModelProviders.of(this).get(PlaceOverviewViewModel::class.java)
        viewModel.getPlaces().observe(this, Observer<List<Place>> { places ->
            placeList.addAll(places)
            viewAdapter.notifyDataSetChanged()
        })

        viewManager = LinearLayoutManager(context)
        viewAdapter = PlaceListAdapter(placeList)

        recyclerView = rootView.findViewById<RecyclerView>(R.id.rv_places).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}
