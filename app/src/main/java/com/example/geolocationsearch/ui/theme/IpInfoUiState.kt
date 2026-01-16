package com.example.geolocationsearch.ui.theme

import com.example.geolocationsearch.data.model.IpInfo

/**
 * Sealed class representing the different states of the IP information UI.
 */
sealed class IpInfoUiState {

    data object Idle : IpInfoUiState()
    data object Loading : IpInfoUiState()
    data class Success(val ipInfo: IpInfo) : IpInfoUiState()
    data class Error(val message: String) : IpInfoUiState()
}
