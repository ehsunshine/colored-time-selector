package de.ehsun.smartbooking.ui.roomdetails.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import de.ehsun.smartbooking.ui.roomdetails.viewholder.EquipmentViewHolder

class EquipmentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val equipments = mutableListOf<String>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) =
            (holder as EquipmentViewHolder).onBindView(equipments[position])

    override fun getItemCount() = equipments.size


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            EquipmentViewHolder.newInstance(parent)

    fun set(equipments: List<String>) {
        this.equipments.clear()
        this.equipments.addAll(equipments)
        notifyDataSetChanged()
    }
}