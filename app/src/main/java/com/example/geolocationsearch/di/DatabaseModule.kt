package com.example.geolocationsearch.di

import android.content.Context
import androidx.room.Room
import com.example.geolocationsearch.data.local.AppDatabase
import com.example.geolocationsearch.data.local.IpInfoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing dependencies related to the database.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ip_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideIpInfoDao(appDatabase: AppDatabase): IpInfoDao {
        return appDatabase.ipInfoDao()
    }
}
