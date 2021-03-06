package de.htwberlin.learningcompanion.places.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.buddy.BuddyFaceHolder
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.model.Place
import de.htwberlin.learningcompanion.places.PlaceListAdapter
import de.htwberlin.learningcompanion.places.details.MyPlaceFragment
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

        updateHeaderLayout()
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
            updateHeaderLayout()
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

        btn_edit_place.onClick {
            navigateToEditPlace()
        }
    }

    private fun updateHeaderLayout() {
        rootView.findViewById<ImageView>(R.id.iv_charlie).setImageDrawable(BuddyFaceHolder.get(context!!).getDefaultFace())

        val placeRepository = PlaceRepository.get(context!!)
        val textView = rootView.findViewById<TextView>(R.id.tv_charlie_info)

        if (placeRepository.placesList?.isEmpty() == true) {
            textView.text = "You don't have added any places yet. \nYou can add new places by clicking on the \"Add\"-Button."
        } else {
            val currentPlace = placeRepository.getCurrentPlace()
            if (currentPlace != null) {
                textView.text = "Your currently selected place is: ${currentPlace.name}"
            } else {
                textView.text = "You do not have a place selected. \nGo ahead and add a new place or choose an existing one."
            }
        }
    }

    private fun navigateToPlaceDetailFragment() {
        val fragment = MyPlaceFragment()
        activity!!.supportFragmentManager.beginTransaction().addToBackStack("detailfragment").replace(R.id.content_main, fragment).commit()
    }

    private fun navigateToEditPlace() {
        val currentPlace = PlaceRepository.get(context!!).getCurrentPlace()

        val fragment = MyPlaceFragment()
        val bundle = Bundle()

        if (currentPlace != null) {
            bundle.putLong("ID", currentPlace!!.id)
            fragment.arguments = bundle
            activity!!.supportFragmentManager.beginTransaction().addToBackStack("detailfragment").replace(R.id.content_main, fragment).commit()
        }

    }
}
