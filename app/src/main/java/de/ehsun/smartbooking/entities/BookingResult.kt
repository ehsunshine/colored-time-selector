package de.ehsun.smartbooking.entities

import com.google.gson.annotations.SerializedName

data class BookingResult(
        @SerializedName("success")
        var success: Boolean)