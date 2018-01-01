package de.ehsun.smartbooking.model.source.network

import de.ehsun.smartbooking.entities.BookingInfo
import de.ehsun.smartbooking.entities.BookingResult
import de.ehsun.smartbooking.entities.Room
import de.ehsun.smartbooking.entities.RoomRequest
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/rooms")
    fun getRooms(@Body roomRequest: RoomRequest): Flowable<List<Room>>

    @POST("/sendpass")
    fun postBookingInfo(@Body bookingInfo: BookingInfo): Flowable<BookingResult>
}