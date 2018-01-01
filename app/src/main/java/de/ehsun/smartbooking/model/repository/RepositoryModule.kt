package de.ehsun.smartbooking.model.repository

import dagger.Module
import dagger.Provides
import de.ehsun.smartbooking.model.repository.booking.BookingRepository
import de.ehsun.smartbooking.model.repository.booking.DefaultBookingRepository
import de.ehsun.smartbooking.model.repository.room.DefaultRoomRepository
import de.ehsun.smartbooking.model.repository.room.RoomRepository
import de.ehsun.smartbooking.model.source.network.ApiService
import de.ehsun.smartbooking.utils.scheduler.SchedulerProvider
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRoomRepository(apiService: ApiService, schedulerProvider: SchedulerProvider):
            RoomRepository = DefaultRoomRepository(apiService, schedulerProvider)

    @Provides
    @Singleton
    fun provideBookingRepository(apiService: ApiService, schedulerProvider: SchedulerProvider):
            BookingRepository = DefaultBookingRepository(apiService, schedulerProvider)
}