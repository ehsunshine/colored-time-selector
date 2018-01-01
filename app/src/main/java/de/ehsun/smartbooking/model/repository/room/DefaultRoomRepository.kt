package de.ehsun.smartbooking.model.repository.room

import de.ehsun.smartbooking.entities.Room
import de.ehsun.smartbooking.entities.RoomFilter
import de.ehsun.smartbooking.entities.RoomOrder
import de.ehsun.smartbooking.entities.RoomRequest
import de.ehsun.smartbooking.model.source.network.ApiService
import de.ehsun.smartbooking.utils.scheduler.SchedulerProvider
import io.reactivex.Flowable

class DefaultRoomRepository(private var apiService: ApiService,
                            private var schedulerProvider: SchedulerProvider) : RoomRepository {

    private var roomList: List<Room>? = null

    override fun getRooms(roomRequest: RoomRequest, sortOrder: RoomOrder, roomFilter: RoomFilter, requestFreshDate: Boolean): Flowable<List<Room>> {
        if (requestFreshDate) {
            roomList = null
        }
        return when (roomList) {
            null -> apiService.getRooms(roomRequest)
                    .subscribeOn(schedulerProvider.ioScheduler)
                    .onErrorReturn {
                        emptyList()
                    }
                    .doOnNext { t ->
                        this.roomList = t
                    }
            else -> Flowable.fromArray(roomList)
        }.map {
            it.filterWith(roomFilter).sortBy(sortOrder)
        }
    }

    private fun List<Room>.sortBy(roomOrder: RoomOrder) = when (roomOrder) {
        RoomOrder.NAME -> this.sortedBy { it.name }
        RoomOrder.LOCATION -> this.sortedBy { it.location }
        RoomOrder.SIZE -> this.sortedByDescending { it.size }
        RoomOrder.CAPACITY -> this.sortedByDescending { it.capacity }
    }

    private fun List<Room>.filterWith(filter: RoomFilter): List<Room> {
        var newRooms = this.filter {
            it.name.contains(filter.name)
        }
        if (filter.isAvailable) {
            newRooms = newRooms.filter {
                !it.available.isEmpty()
            }
        }
        return newRooms
    }

}