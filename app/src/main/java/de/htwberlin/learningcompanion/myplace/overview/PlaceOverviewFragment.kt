package de.htwberlin.learningcompanion.myplace.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.model.Place
import de.htwberlin.learningcompanion.myplace.details.MyPlaceFragment
import de.htwberlin.learningcompanion.ui.PlaceListAdapter
import de.htwberlin.learningcompanion.util.setActivityTitle
import kotlinx.android.synthetic.main.fragment_place_overview.*
import org.jetbrains.anko.sdk27.coroutines.onClick


class PlaceOverviewFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: PlaceListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var viewModel: PlaceOverviewViewModel

    private lateinit var rootView: View
    private val placeList = ArrayList<Place>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_place_overview, container, false)

        initHeaderLayout()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setActivityTitle(getString(R.string.title_nav_menu_place))

        viewModel = ViewModelProviders.of(this).get(PlaceOverviewViewModel::class.java)
        viewModel.getPlaces().observe(this, Observer<List<Place>> { places ->
            placeList.clear()
            placeList.addAll(places)
            viewAdapter.notifyDataSetChanged()
        })

        viewManager = LinearLayoutManager(context)
        viewAdapter = PlaceListAdapter(placeList, activity!!.supportFragmentManager)

        recyclerView = rootView.findViewById<RecyclerView>(R.id.rv_places).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }

        btn_new_place.onClick {
            navigateToPlaceDetailFragment()
        }
    }

    private fun initHeaderLayout() {
        val currentPlace = PlaceRepository.get(context!!).getCurrentPlace()
        if (currentPlace != null) {
            rootView.findViewById<TextView>(R.id.tv_current_place_displayname).text = currentPlace.addressString
        }
    }

    private fun navigateToPlaceDetailFragment() {
        val fragment = MyPlaceFragment()
        activity!!.supportFragmentManager.beginTransaction().addToBackStack("detailfragment").replace(R.id.content_main, fragment).commit()
    }
}
