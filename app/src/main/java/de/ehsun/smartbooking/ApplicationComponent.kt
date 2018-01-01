package de.ehsun.smartbooking

import dagger.Component
import de.ehsun.smartbooking.model.repository.RepositoryModule
import de.ehsun.smartbooking.model.source.SourceModule
import de.ehsun.smartbooking.ui.PresentersModule
import de.ehsun.smartbooking.ui.launch.LaunchActivity
import de.ehsun.smartbooking.ui.main.MainActivity
import de.ehsun.smartbooking.ui.roomdetails.RoomDetailsActivity
import de.ehsun.smartbooking.utils.UtilsModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        PresentersModule::class,
        SourceModule::class,
        RepositoryModule::class,
        UtilsModule::class))
interface ApplicationComponent {

    fun inject(launchActivity: LaunchActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(roomDetailsActivity: RoomDetailsActivity)
}