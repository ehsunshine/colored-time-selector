package de.ehsun.smartbooking.entities

import com.google.gson.annotations.SerializedName

data class Attendee(@SerializedName("name") var name: String,
                    @SerializedName("email") var email: String,
                    @SerializedName("number") var phone: String) {
    constructor() : this("", "", "")
}