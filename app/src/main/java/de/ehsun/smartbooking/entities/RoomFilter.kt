package de.ehsun.smartbooking.entities

data class RoomFilter(val name: String,
                      val isAvailable: Boolean) {

    companion object {
        @JvmStatic
        val ALL_BOOKS = RoomFilter(
                name = "",
                isAvailable = true
        )
    }
}