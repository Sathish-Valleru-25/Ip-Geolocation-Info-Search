package com.example.geolocationsearch.data.repos

import android.util.Log
import androidx.compose.animation.core.copy
import com.example.geolocationsearch.data.local.IpInfoDao
import com.example.geolocationsearch.data.model.IpInfo
import com.example.geolocationsearch.data.remote.IpApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Implementation of the IpSearchRepository interface.
 * This class is responsible for fetching IpInfo data from the remote API and local database.
 */
class IpAddressSearchRepositoryImpl  @Inject constructor(
    private val apiService: IpApiService,
    private val ipInfoDao: IpInfoDao
) :

    IpSearchRepository {
    override fun getIpInfo(ipAddress: String): Flow<Result<IpInfo>> = flow {
        //  fetch data from the local database
        val localData = ipInfoDao.getIpInfoByIp(ipAddress)
        val fiveMinutesInMillis = TimeUnit.MINUTES.toMillis(5)

        // make sure the data is not older than 5 minutes, if it is then make a api request to get the data
        if (localData != null && (System.currentTimeMillis() - localData.createdAt) < fiveMinutesInMillis) {
            emit(Result.success(localData))
        } else {
            // if ip address data does not exist in DB then make a api request to get the data
            try {
                val  response = apiService.getIpInfo(ipAddress)

                if (response.isSuccessful) {
                    response.body()?.let { networkData ->

                        val finalData = networkData.copy(createdAt = System.currentTimeMillis())
                        ipInfoDao.insertIpInfo(finalData)
                        emit(Result.success(networkData))
                        Log.d("IpRepoImpl", "API fetch successful for $ipAddress")

                    } ?: run {
                        emit(Result.failure(Exception("API Error: Response body was null")))
                    }

                } else {
                    emit(Result.failure(Exception("API Error: ${response.message()}")))
                    Log.d("IpRepoImpl", "API fetch failed for $ipAddress")
                }
            } catch (e: Exception) {
                emit(Result.failure(e))
                Log.d("IpRepoImpl", "API fetch failed $ipAddress ${e.message}")
            }
        }
    }
}