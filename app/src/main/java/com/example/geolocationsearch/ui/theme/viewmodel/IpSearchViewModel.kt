package com.example.geolocationsearch.ui.theme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geolocationsearch.data.repos.IpSearchRepository
import com.example.geolocationsearch.ui.theme.IpInfoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IpSearchViewModel @Inject constructor(
    private val ipRepository: IpSearchRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<IpInfoUiState>(IpInfoUiState.Idle)
    val uiState: StateFlow<IpInfoUiState> = _uiState.asStateFlow()

    fun fetchIpInformation(ipAddress: String) {

        if (_uiState.value is IpInfoUiState.Loading) {
            return
        }

        // fetch IpInfo from local db or remote.
        viewModelScope.launch {
            ipRepository.getIpInfo(ipAddress)
                .onStart {
                    _uiState.value = IpInfoUiState.Loading  // Set loading state
                }.catch { exception ->
                    _uiState.value = IpInfoUiState.Error("An unexpected error occurred: ${exception.message}")
                }
                .collect { result ->
                    // The result from the repository is either a success or a failure
                    result.onSuccess { ipInfo ->
                        // if this result is 200 check IpInfo object status is success or fail
                        if(ipInfo.status == "success") {
                            _uiState.value = IpInfoUiState.Success(ipInfo)
                        } else {
                            _uiState.value = IpInfoUiState.Error("Geolocation Information is not available for this ip address: ${ipInfo.query}")
                        }
                    }.onFailure { exception ->
                        _uiState.value = IpInfoUiState.Error(exception.message ?: "An unknown error occurred")
                    }
                }
        }
    }

}