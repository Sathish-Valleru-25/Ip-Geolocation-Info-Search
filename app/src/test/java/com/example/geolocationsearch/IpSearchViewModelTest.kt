package com.example.geolocationsearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.geolocationsearch.data.model.IpInfo
import com.example.geolocationsearch.data.repos.IpSearchRepository
import com.example.geolocationsearch.ui.theme.IpInfoUiState
import com.example.geolocationsearch.ui.theme.viewmodel.IpSearchViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val mockRepository: IpSearchRepository = mockk()

    private lateinit var viewModel: IpSearchViewModel

    @Test
    fun `searchIpInfo with valid IP, when repository returns success, then UiState is Success`() =
        runTest {
            val ipAddress = "52.10.25.25"
            val expectedIpInfo =
                IpInfo(
                    query = ipAddress,
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

            coEvery { mockRepository.getIpInfo(ipAddress) } returns flowOf(Result.success(expectedIpInfo))
            viewModel = IpSearchViewModel(mockRepository)
            viewModel.fetchIpInformation(ipAddress)
            val finalState =
                viewModel.uiState.first()
            assertTrue(finalState is IpInfoUiState.Success)
            assertEquals(expectedIpInfo, (finalState as IpInfoUiState.Success).ipInfo)
        }

    @Test
    fun `searchIpInfo, when repository returns failure, then UiState is Error`() = runTest {

        val ipAddress = "invalid-ip"
        val errorMessage = "Network error"
        coEvery { mockRepository.getIpInfo(ipAddress) } returns
                flowOf(Result.failure(
                    RuntimeException(
                        errorMessage
                    ))
        )
        viewModel = IpSearchViewModel(mockRepository)

        viewModel.fetchIpInformation(ipAddress)

        val finalState = viewModel.uiState.first()
        assertTrue(finalState is IpInfoUiState.Error)
        assertEquals(errorMessage, (finalState as IpInfoUiState.Error).message)
    }
}