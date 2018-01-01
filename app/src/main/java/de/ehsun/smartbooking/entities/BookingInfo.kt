package de.ehsun.smartbooking.entities

import com.google.gson.annotations.SerializedName

data class BookingInfo(@SerializedName("booking") var booking: Booking,
                       @SerializedName("passes") var attendees: List<Attendee>) {
    constructor() : this(Booking(), ArrayList())
}