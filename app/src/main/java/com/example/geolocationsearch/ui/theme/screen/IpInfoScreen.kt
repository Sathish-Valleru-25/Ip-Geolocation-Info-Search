package com.example.geolocationsearch.ui.theme.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.geolocationsearch.data.model.IpInfo
import com.example.geolocationsearch.ui.theme.IpInfoUiState
import com.example.geolocationsearch.ui.theme.viewmodel.IpSearchViewModel
import com.example.geolocationsearch.ui.theme.utils.isValidIpAddress

/**
 * The main composable for the IP geolocation search screen.
 */
@Composable
fun IpInfoScreen(
    viewModel: IpSearchViewModel =hiltViewModel()
) {

    var ipAddressInput by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val isInputValid by remember {
        derivedStateOf {
            isValidIpAddress(ipAddressInput)
        }
    }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "IP Geolocation Finder",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = ipAddressInput,
            onValueChange = { ipAddressInput = it },
            label = { Text("Enter IP Address") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (isInputValid) {
                        viewModel.fetchIpInformation(ipAddressInput)
                        focusManager.clearFocus()
                    }
                }
            ),       singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("ip_input_field"),
                    isError = !isInputValid && ipAddressInput.isNotEmpty(),
            supportingText = {
                if (!isInputValid && ipAddressInput.isNotEmpty()) {
                    Text("Please enter a valid IPv4 address")
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (isInputValid) {
                    viewModel.fetchIpInformation(ipAddressInput)
                    focusManager.clearFocus()
                }
            },
            // Disable button while loading
            enabled = uiState !is IpInfoUiState.Loading,
            modifier = Modifier.fillMaxWidth().testTag("search_button")
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is IpInfoUiState.Idle -> {
                    Text(text = "Enter an IP address and press Search.")
                }
                is IpInfoUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is IpInfoUiState.Success -> {
                    // On success, show the results
                    IpInfoResult(ipInfo = state.ipInfo)
                }
                is IpInfoUiState.Error -> {
                    // On error, show the error message
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("error_message_text")
                    )
                }
            }
        }
    }
}

/**
 * A separate composable to display the successful result.
 * This keeps the main screen composable cleaner.
 */
@Composable
fun IpInfoResult(ipInfo: IpInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Query: ${ipInfo.query}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            InfoRow(label = "Status", value = ipInfo.status ?:"" ,  modifier = Modifier.testTag("status_row")
            )
            InfoRow(label = "Location", value = "${ipInfo.city}, ${ipInfo.regionName}, ${ipInfo.country}",  modifier = Modifier.testTag("location_row")
            )
            InfoRow(label = "ZipCode", value = "${ipInfo.zip}",  modifier = Modifier.testTag("zip_row")
            )
            InfoRow(label = "TimeZone", value = "${ipInfo.timezone}",   modifier = Modifier.testTag("timezone_row")
            )
            InfoRow(label = "Coordinates", value = "Lat: ${ipInfo.lat}, Lon: ${ipInfo.lon}",  modifier = Modifier.testTag("coordinates_row") )
            InfoRow(label = "ISP", value = ipInfo.isp ?: "N/A,",  modifier = Modifier.testTag("ISP_row")
            )
            InfoRow(label = "Organization", value = ipInfo.org ?: "N/A",  modifier = Modifier.testTag("status_row")
            )
        }
    }
}

/**
 * A helper composable for displaying a labeled piece of information.
 */
@Composable
fun InfoRow(label: String, value: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "$label: ",
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(120.dp) // Align values
        )
        Text(text = value)
    }
    Spacer(modifier = Modifier.height(8.dp))
}
