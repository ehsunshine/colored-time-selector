package de.ehsun.smartbooking.utils

import dagger.Module
import dagger.Provides
import de.ehsun.smartbooking.utils.scheduler.DefaultSchedulerProvider
import de.ehsun.smartbooking.utils.scheduler.SchedulerProvider

@Module
class UtilsModule {

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider =
            DefaultSchedulerProvider()

}