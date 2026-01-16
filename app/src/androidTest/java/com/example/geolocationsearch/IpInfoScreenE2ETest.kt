package com.example.geolocationsearch


import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest

class IpInfoScreenE2ETest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        if (::mockWebServer.isInitialized) {
            mockWebServer.shutdown()
        }
    }

    @Test
    fun onSuccessfulSearch_displaysCorrectDataOnScreen() {
         val successResponseJson = """
         {"status":"success","country":"United States","countryCode":"US","region":"OR","regionName":"Oregon","city":"Portland","zip":"97207","lat":45.5235,"lon":-122.676,"timezone":"America/Los_Angeles","isp":"Amazon.com, Inc.","org":"AWS EC2 (us-west-2)","as":"AS16509 Amazon.com, Inc.","query":"52.10.25.25"}
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(successResponseJson)
        )

        composeTestRule.onNodeWithTag("ip_input_field")
            .performTextInput("52.10.25.25")
        composeTestRule.onNodeWithTag("search_button")
            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithTag("location_row").fetchSemanticsNodes().size == 1
        }
        composeTestRule.onNodeWithText("Location: ").assertIsDisplayed()
        composeTestRule.onNodeWithText("Portland, Oregon, United States").assertIsDisplayed()
        composeTestRule.onNodeWithText("ISP: ").assertIsDisplayed()
        composeTestRule.onNodeWithText("Amazon.com, Inc.").assertIsDisplayed()
    }

    @Test
    fun onFailedSearch_displaysErrorMessageOnScreen() {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error")
        )
        composeTestRule.onNodeWithTag("ip_input_field")
            .performTextInput("8.8.8.8")
        composeTestRule.onNodeWithTag("search_button")
            .performClick()
        val errorNode = composeTestRule.onNodeWithTag("error_message_text")
        errorNode.assertIsDisplayed()
        errorNode.assert(hasText("API Error: Server Error"))

    }
}
