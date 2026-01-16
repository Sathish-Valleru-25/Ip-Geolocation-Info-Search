package com.example.geolocationsearch.data.remote

import com.example.geolocationsearch.data.model.IpInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface IpApiService {
    @GET("json/{ipAddress}")
     suspend fun getIpInfo(@Path("ipAddress") name: String) : Response<IpInfo>
}