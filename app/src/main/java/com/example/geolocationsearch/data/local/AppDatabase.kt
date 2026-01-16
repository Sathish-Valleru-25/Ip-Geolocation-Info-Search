package com.example.geolocationsearch.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.geolocationsearch.data.model.IpInfo

/**
 * The Room database for this app
 */
@Database(entities = [IpInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ipInfoDao(): IpInfoDao
}
