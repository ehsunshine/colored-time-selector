package de.ehsun.smartbooking

import android.app.Application
import de.ehsun.smartbooking.model.repository.RepositoryModule
import de.ehsun.smartbooking.model.source.SourceModule
import de.ehsun.smartbooking.ui.PresentersModule
import de.ehsun.smartbooking.utils.UtilsModule
import net.danlew.android.joda.JodaTimeAndroid

class BookLibraryApplication : Application() {

    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent
                .builder()
                .presentersModule(PresentersModule())
                .sourceModule(SourceModule(this))
                .repositoryModule(RepositoryModule())
                .utilsModule(UtilsModule())
                .build()
    }

    companion object {
        lateinit var instance: BookLibraryApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        JodaTimeAndroid.init(this)
    }
}