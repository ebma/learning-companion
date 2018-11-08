package de.htwberlin.learningcompanion.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.htwberlin.learningcompanion.R
import de.htwberlin.learningcompanion.model.Place

class PlaceListAdapter(private val placeDataSet: ArrayList<Place>) : RecyclerView.Adapter<PlaceListAdapter.PlaceViewHolder>() {

    class PlaceViewHolder(val rootView: View) : RecyclerView.ViewHolder(rootView), View.OnClickListener {
        private var place: Place? = null

        private val tvName: TextView
        private val tvAddress: TextView
        private val ivPlacePreview: ImageView

        init {
            rootView.setOnClickListener(this)
            tvName = rootView.findViewById(R.id.tv_name)
            tvAddress = rootView.findViewById(R.id.tv_address)
            ivPlacePreview = rootView.findViewById(R.id.iv_place_preview)
        }

        override fun onClick(v: View) {
            val context = itemView.context
//            val showPhotoIntent = Intent(context, PhotoActivity::class.java)
//            showPhotoIntent.putExtra(PHOTO_KEY, photo)
//            context.startActivity(showPhotoIntent)
        }

        fun bindPlace(place: Place) {
            this.place = place
            Picasso.get().load(place.imageUri).fit().into(ivPlacePreview)
            tvName.text = place.name
            tvAddress.text = place.addressString
        }

        companion object {
            private val PHOTO_KEY = "PHOTO"
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceListAdapter.PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_list_item, parent, false) as View
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = placeDataSet[position]
        holder.bindPlace(place)
    }

    override fun getItemCount() = placeDataSet.size

}