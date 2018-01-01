package de.ehsun.smartbooking.ui.main.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import de.ehsun.smartbooking.entities.Room
import de.ehsun.smartbooking.ui.main.viewholder.LocationViewHolder
import de.ehsun.smartbooking.ui.main.viewholder.RoomViewHolder
import de.ehsun.smartbooking.ui.main.viewholder.RoomViewHolder.RoomListener

class RoomAdapter(var roomEventListener: RoomEventListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_TYPE_LOCATION = 1
        const val ITEM_TYPE_BOOK = 2
    }

    private val items = mutableListOf<Item>()
    private val rooms = mutableListOf<Room>()
    private val roomViewHolder = object : RoomListener {
        override fun onRoomClick(room: Room) {
            roomEventListener.onRoomItemClick(room)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            when (viewType) {
                ITEM_TYPE_BOOK -> RoomViewHolder.newInstance(parent, roomViewHolder)
                ITEM_TYPE_LOCATION -> LocationViewHolder.newInstance(parent)
                else -> RoomViewHolder.newInstance(parent, roomViewHolder)
            }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = items[position].type

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (holder) {
            is LocationViewHolder -> {
                val location = (items[position] as Item.LocationItem).title
                holder.onBindView(location)
            }
            is RoomViewHolder -> {
                val room = (items[position] as Item.RoomItem).room
                holder.onBindView(room)
            }
        }
    }

    fun set(rooms: List<Room>, showSections: Boolean) {
        this.rooms.clear()
        this.rooms.addAll(rooms)
        regenerateItems(showSections)
        notifyDataSetChanged()
    }

    private fun regenerateItems(showSections: Boolean) {
        this.items.clear()
        this.items += when (showSections) {
            true -> rooms.groupBy { it.location }
                    .flatMap { (location, books) ->
                        val items = mutableListOf<Item>()
                        items.add(Item.LocationItem(location))
                        items.addAll(books.map { Item.RoomItem(it) })
                        items
                    }
            false -> rooms.map { Item.RoomItem(it) }
        }
    }

    sealed class Item(val type: Int) {
        class LocationItem(val title: String) : Item(ITEM_TYPE_LOCATION)
        class RoomItem(val room: Room) : Item(ITEM_TYPE_BOOK)
    }

    interface RoomEventListener {
        fun onRoomItemClick(room: Room)
    }
}