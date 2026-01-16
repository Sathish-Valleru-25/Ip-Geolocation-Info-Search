package com.example.geolocationsearch

import com.example.geolocationsearch.data.remote.IpApiService
import com.example.geolocationsearch.di.ApiModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ApiModule::class]
)
object TestApiModule {

    /**
     * Provides a Retrofit instance that is hardcoded to point to the
     * address our MockWebServer will be running on.
     */
    @Provides
    @Singleton
    fun provideTestIpApiService(): IpApiService {
        val okHttpClient = OkHttpClient.Builder().build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://localhost:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IpApiService::class.java)
    }
}
