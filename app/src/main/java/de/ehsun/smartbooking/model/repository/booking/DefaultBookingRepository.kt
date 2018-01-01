package de.ehsun.smartbooking.model.repository.booking

import de.ehsun.smartbooking.entities.BookingInfo
import de.ehsun.smartbooking.entities.BookingResult
import de.ehsun.smartbooking.model.source.network.ApiService
import de.ehsun.smartbooking.utils.scheduler.SchedulerProvider
import io.reactivex.Flowable

class DefaultBookingRepository(private var apiService: ApiService,
                               private var schedulerProvider: SchedulerProvider) : BookingRepository {

    override fun saveBookingInfo(bookingInfo: BookingInfo): Flowable<BookingResult> {
        return apiService.postBookingInfo(bookingInfo)
                .subscribeOn(schedulerProvider.ioScheduler)
                .onErrorReturn {
                    BookingResult(success = false)
                }.onErrorResumeNext { t: Throwable -> Flowable.error(t) }
    }
}