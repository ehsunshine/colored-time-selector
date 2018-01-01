package de.ehsun.smartbooking.ui.main.viewholder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import de.ehsun.smartbooking.R
import de.ehsun.smartbooking.entities.Room
import de.ehsun.smartbooking.utils.extension.dp2px
import kotlinx.android.synthetic.main.list_item_room_item.view.*

class RoomViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private var roomListener: RoomListener? = null

        fun newInstance(parent: ViewGroup, roomListener: RoomListener): RoomViewHolder {
            this.roomListener = roomListener
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_room_item, parent, false)
            return RoomViewHolder(view)
        }
    }

    fun onBindView(room: Room) {
        with(itemView) {
            roomNameTextView.text = context.getString(R.string.room_number, room.name)
            roomSizeTextView.text = context.getString(R.string.room_size, room.size)

            Picasso.with(context)
                    .load(room.images[0])
                    .placeholder(R.drawable.ic_photo_24dp)
                    .error(R.drawable.ic_photo_24dp)
                    .resize(context.dp2px(88f).toInt(), context.dp2px(88f).toInt())
                    .centerCrop()
                    .into(roomPicImageView)

            timelineView.setAvailableTimeRange(room.available)

            roomContainer.setOnClickListener { roomListener?.onRoomClick(room) }
        }
    }

    interface RoomListener {
        fun onRoomClick(room: Room)
    }
}