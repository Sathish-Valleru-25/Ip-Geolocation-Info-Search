package com.example.geolocationsearch

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.geolocationsearch.data.local.AppDatabase
import com.example.geolocationsearch.data.local.IpInfoDao
import com.example.geolocationsearch.data.model.IpInfo
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class IpInfoDaoTest {

    private lateinit var ipInfoDao: IpInfoDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        ipInfoDao = db.ipInfoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetIpInfo_succeeds() = runBlocking {
        val ipInfoToInsert =  IpInfo(
            query = "52.10.25.25",
            status = "success",
            country = "United States",
            countryCode ="US",
            region = "OR",
            regionName = "Oregon",
            city = "Portland",
            zip = "97207",
            lat = 45.5235,
            lon = -122.676,
            timezone = "America/Los_Angeles",
            isp = "Amazon.com, Inc.",
            org = "AWS EC2 (us-west-2)",
            asName = "AS16509 Amazon.com, Inc.",
            createdAt =  System.currentTimeMillis()
        )
        ipInfoDao.insertIpInfo(ipInfoToInsert)
        val retrievedInfo = ipInfoDao.getIpInfoByIp("52.10.25.25")
        assertEquals(ipInfoToInsert, retrievedInfo)
    }

    @Test
    fun getIpInfo_forNonExistentIp_returnsNull() = runBlocking {
        val nonExistentIp = "52.10.25.25"
        val retrievedInfo = ipInfoDao.getIpInfoByIp(
            nonExistentIp)
        assertNull(retrievedInfo)
    }
}
