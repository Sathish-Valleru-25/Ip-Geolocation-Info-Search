package com.example.geolocationsearch.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ip_info_table")
data class IpInfo(
    @PrimaryKey
 @SerializedName("query")
 var query : String ,
 @SerializedName("status") 
 var status: String? ,
 @SerializedName("country")
 var country  : String? ,
 @SerializedName("countryCode" )
 var countryCode : String? ,
 @SerializedName("region")
 var region: String? ,
 @SerializedName("regionName")
 var regionName  : String? ,
 @SerializedName("city")
 var city  : String? ,
 @SerializedName("zip")
 var zip: String? ,
 @SerializedName("lat")
 var lat: Double? ,
 @SerializedName("lon")
 var lon: Double? ,
 @SerializedName("timezone" )
 var timezone : String? ,
 @SerializedName("isp")
 var isp: String? ,
 @SerializedName("org")
 var org: String? ,
 @SerializedName("as" )
 var asName : String?,
    @ColumnInfo(name = "created_at")
val createdAt: Long = System.currentTimeMillis()

)

