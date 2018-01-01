package de.ehsun.smartbooking.ui

import dagger.Module
import dagger.Provides
import de.ehsun.smartbooking.model.repository.booking.BookingRepository
import de.ehsun.smartbooking.model.repository.room.RoomRepository
import de.ehsun.smartbooking.ui.launch.LaunchContract
import de.ehsun.smartbooking.ui.launch.LaunchPresenter
import de.ehsun.smartbooking.ui.main.MainContract
import de.ehsun.smartbooking.ui.main.MainPresenter
import de.ehsun.smartbooking.ui.roomdetails.RoomDetailsContract
import de.ehsun.smartbooking.ui.roomdetails.RoomDetailsPresenter
import de.ehsun.smartbooking.utils.scheduler.SchedulerProvider

@Module
class PresentersModule {

    @Provides
    fun provideLaunchPresenter(): LaunchContract.Presenter {
        return LaunchPresenter()
    }

    @Provides
    fun provideMainPresenter(roomRepository: RoomRepository,
                             schedulerProvider: SchedulerProvider): MainContract.Presenter =
            MainPresenter(roomRepository, schedulerProvider)

    @Provides
    fun provideRoomDetailsPresenter(bookingRepository: BookingRepository,
                                    schedulerProvider: SchedulerProvider): RoomDetailsContract.Presenter =
            RoomDetailsPresenter(bookingRepository, schedulerProvider)
}