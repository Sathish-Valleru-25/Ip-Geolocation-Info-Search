package com.example.geolocationsearch.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.geolocationsearch.data.model.IpInfo

/**
 * Data Access Object for the ip_info_table.
 */
@Dao
interface IpInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIpInfo(ipInfo: IpInfo)
    @Query("SELECT * FROM ip_info_table WHERE `query` = :ipAddress")
    suspend fun getIpInfoByIp(ipAddress: String): IpInfo?



}