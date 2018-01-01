package de.ehsun.smartbooking.model.repository.room

import de.ehsun.smartbooking.entities.Room
import de.ehsun.smartbooking.entities.RoomFilter
import de.ehsun.smartbooking.entities.RoomOrder
import de.ehsun.smartbooking.entities.RoomRequest
import io.reactivex.Flowable

interface RoomRepository {
    fun getRooms(roomRequest: RoomRequest, sortOrder: RoomOrder, roomFilter: RoomFilter, requestFreshDate: Boolean): Flowable<List<Room>>
}