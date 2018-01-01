package de.ehsun.smartbooking.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Room(@SerializedName("name") var name: String,
                @SerializedName("location") var location: String,
                @SerializedName("size") var size: String,
                @SerializedName("capacity") var capacity: Int,
                @SerializedName("equipment") var equipment: List<String>,
                @SerializedName("avail") var available: List<String>,
                @SerializedName("images") var images: List<String>) : Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.createStringArrayList(),
            parcel.createStringArrayList(),
            parcel.createStringArrayList())

    constructor() : this("", "", "", 0, ArrayList(), ArrayList(), ArrayList())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(location)
        parcel.writeString(size)
        parcel.writeInt(capacity)
        parcel.writeStringList(equipment)
        parcel.writeStringList(available)
        parcel.writeStringList(images)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Room> {
        override fun createFromParcel(parcel: Parcel) = Room(parcel)

        override fun newArray(size: Int): Array<Room?> = arrayOfNulls(size)
    }


}