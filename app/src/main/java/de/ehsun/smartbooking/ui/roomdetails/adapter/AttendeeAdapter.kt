package de.ehsun.smartbooking.ui.roomdetails.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import de.ehsun.smartbooking.entities.Attendee
import de.ehsun.smartbooking.ui.roomdetails.viewholder.AttendeeViewHolder

class AttendeeAdapter(var attendeeEventListener: AttendeeEventListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val attendees = mutableListOf<Attendee>()

    private val attendeeListener = object : AttendeeViewHolder.AttendeeListener {
        override fun onCloseClick(attendee: Attendee) {
            attendeeEventListener.onAttendeeItemCloseClick(attendee)
        }

        override fun onAttendeeClick(attendee: Attendee) {
            attendeeEventListener.onAttendeeItemClick(attendee)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) =
            (holder as AttendeeViewHolder).onBindView(attendees[position])

    override fun getItemCount() = attendees.size


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            AttendeeViewHolder.newInstance(parent, attendeeListener)

    fun set(attendees: List<Attendee>) {
        this.attendees.clear()
        this.attendees.addAll(attendees)
        notifyDataSetChanged()
    }

    interface AttendeeEventListener {
        fun onAttendeeItemClick(attendee: Attendee)
        fun onAttendeeItemCloseClick(attendee: Attendee)
    }
}