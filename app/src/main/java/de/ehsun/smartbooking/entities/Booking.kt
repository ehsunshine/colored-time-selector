package de.ehsun.smartbooking.entities

import com.google.gson.annotations.SerializedName

class Booking(@SerializedName("date") var date: Long,
              @SerializedName("time_start") var startTime: Long,
              @SerializedName("time_end") var endTime: Long,
              @SerializedName("title") var title: String,
              @SerializedName("description") var description: String,
              @SerializedName("room") var room: String) {
    constructor() : this(0, 0, 0, "", "", "")
}