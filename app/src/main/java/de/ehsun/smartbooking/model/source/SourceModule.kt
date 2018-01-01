package de.ehsun.smartbooking.model.source

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import de.ehsun.smartbooking.model.source.config.BaseConfiguration
import de.ehsun.smartbooking.model.source.config.DefaultBaseConfiguration
import de.ehsun.smartbooking.model.source.network.ApiService
import de.ehsun.smartbooking.model.source.network.ServiceUtils
import de.ehsun.smartbooking.utils.gson.DateTimeDeserializer
import de.ehsun.smartbooking.utils.gson.LocalDateDeserializer
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import org.joda.time.LocalDate
import javax.inject.Singleton

@Module
class SourceModule(private var context: Context) {

    companion object {
        const val SHARED_PREF_BOOKING = "BOOKING_PREFS"
    }

    @Provides
    @Singleton
    fun provideSharedPreference() = context
            .getSharedPreferences(SHARED_PREF_BOOKING, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideGson() = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(DateTime::class.java, DateTimeDeserializer())
            .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
            .create()

    @Provides
    @Singleton
    fun provideBaseConfiguration(): BaseConfiguration {
        return DefaultBaseConfiguration()
    }

    @Provides
    @Singleton
    fun provideApiService(gson: Gson, baseConfiguration: BaseConfiguration): ApiService {
        val retrofit = ServiceUtils.createDefaultRetrofitBuilder(
                baseConfiguration.getServerBaseUrl(),
                gson,
                ServiceUtils.getLoggingInterceptor(HttpLoggingInterceptor.Level.BODY))

        return retrofit.create(ApiService::class.java)
    }
}