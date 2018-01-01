package de.ehsun.smartbooking.model.source.network

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceUtils {

    fun createDefaultRetrofitBuilder(baseUrl: String, gson: Gson, vararg interceptors: Interceptor): Retrofit {

        val okHttpClientBuilder = createDefaultClient()
        if (interceptors.isNotEmpty()) {
            for (interceptor in interceptors) {
                okHttpClientBuilder.interceptors().add(interceptor)
            }
        }

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClientBuilder.build())
                .build()
    }

    fun getLoggingInterceptor(logLevel: HttpLoggingInterceptor.Level): Interceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = logLevel
        return loggingInterceptor
    }

    private fun createDefaultClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
                .followSslRedirects(true)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
    }
}