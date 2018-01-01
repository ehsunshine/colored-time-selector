package de.ehsun.smartbooking.ui.main.viewholder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.ehsun.smartbooking.R
import kotlinx.android.synthetic.main.list_item_location_item.view.*

class LocationViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun newInstance(parent: ViewGroup?): LocationViewHolder {
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_location_item, parent, false)
            return LocationViewHolder(view)
        }
    }

    fun onBindView(title: String) {
        with(itemView) {
            locationName.text = title
        }
    }

}