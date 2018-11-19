package de.htwberlin.learningcompanion.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.db.PlaceRepository
import de.htwberlin.learningcompanion.model.Place
import de.htwberlin.learningcompanion.places.details.MyPlaceFragment
import org.jetbrains.anko.sdk27.coroutines.onClick

class PlaceListAdapter(private val placeDataSet: ArrayList<Place>, val supportFragmentManager: FragmentManager) : RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder>() {

    class PlaceViewHolder(val rootView: View, private val supportFragmentManager: FragmentManager) : RecyclerView.ViewHolder(rootView) {
        private lateinit var place: Place

        private val tvName: TextView
        private val tvAddress: TextView
        private val ivPlacePreview: ImageView

        private val btnEdit: ImageButton
        private val cbSetCurrent: CheckBox

        init {
            tvName = rootView.findViewById(R.id.tv_name)
            tvAddress = rootView.findViewById(R.id.tv_address)
            ivPlacePreview = rootView.findViewById(R.id.iv_place_preview)

            btnEdit = rootView.findViewById(R.id.btn_edit)
            btnEdit.onClick { navigateToPlaceDetailFragment() }
            cbSetCurrent = rootView.findViewById(R.id.cb_current_place)
        }

        fun bindPlace(place: Place) {
            this.place = place
            tvName.text = place.name
            tvAddress.text = place.addressString

            cbSetCurrent.isChecked = place.currentPlace

            cbSetCurrent.onClick {
                PlaceRepository.get(itemView.context).setPlaceAsCurrentPlace(place)
            }

            Picasso.get().load(place.imageUri).fit().into(ivPlacePreview)
        }

        private fun navigateToPlaceDetailFragment() {
            val fragment = MyPlaceFragment()
            val bundle = Bundle()
            bundle.putLong("ID", place.id)
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().addToBackStack("detailfragment").replace(R.id.content_main, fragment).commit()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_list_item, parent, false) as View
        return PlaceViewHolder(view, supportFragmentManager)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = placeDataSet[position]
        holder.bindPlace(place)
    }

    override fun getItemCount() = placeDataSet.size

}