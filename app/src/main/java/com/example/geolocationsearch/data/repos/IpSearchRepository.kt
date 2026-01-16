package com.example.geolocationsearch.data.repos
import com.example.geolocationsearch.data.model.IpInfo
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for fetching IpInfo data.
 */
interface IpSearchRepository {
    fun getIpInfo(ipAddress: String): Flow<Result<IpInfo>>
}