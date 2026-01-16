package com.example.geolocationsearch.ui.theme.utils

import android.net.InetAddresses

/**
 * Checks if the given string is a valid IPv4 address.
 */
fun isValidIpAddress(ipAddress: String): Boolean {
    return InetAddresses.isNumericAddress(ipAddress)
}