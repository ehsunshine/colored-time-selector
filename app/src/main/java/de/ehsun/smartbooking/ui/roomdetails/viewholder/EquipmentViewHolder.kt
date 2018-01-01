package de.ehsun.smartbooking.ui.roomdetails.viewholder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.ehsun.smartbooking.R
import kotlinx.android.synthetic.main.list_item_equipment_item.view.*

class EquipmentViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun newInstance(parent: ViewGroup?): EquipmentViewHolder {
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_equipment_item, parent, false)
            return EquipmentViewHolder(view)
        }
    }

    fun onBindView(title: String) {
        with(itemView) {
            equipmentName.text = title
        }
    }

}