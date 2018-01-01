package de.ehsun.smartbooking.model.repository.booking

import de.ehsun.smartbooking.entities.BookingInfo
import de.ehsun.smartbooking.entities.BookingResult
import io.reactivex.Flowable

interface BookingRepository {
    fun saveBookingInfo(bookingInfo: BookingInfo): Flowable<BookingResult>
}