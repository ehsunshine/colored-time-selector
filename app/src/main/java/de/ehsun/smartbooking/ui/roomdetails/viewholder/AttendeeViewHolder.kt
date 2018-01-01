package de.ehsun.smartbooking.ui.roomdetails.viewholder

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.ehsun.smartbooking.R
import de.ehsun.smartbooking.entities.Attendee
import kotlinx.android.synthetic.main.list_item_attendee_item.view.*

class AttendeeViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private var attendeeListener: AttendeeListener? = null

        fun newInstance(parent: ViewGroup?, attendeeListener: AttendeeListener): AttendeeViewHolder {
            this.attendeeListener = attendeeListener
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_attendee_item, parent, false)
            return AttendeeViewHolder(view)
        }
    }

    fun onBindView(attendee: Attendee) {
        with(itemView) {
            attendeeNameTextView.text = attendee.name
            attendeeRemoveImageView.setOnClickListener { attendeeListener?.onCloseClick(attendee) }
            attendeeContainer.setOnClickListener { attendeeListener?.onAttendeeClick(attendee) }
        }
    }

    interface AttendeeListener {
        fun onCloseClick(attendee: Attendee)
        fun onAttendeeClick(attendee: Attendee)
    }
}