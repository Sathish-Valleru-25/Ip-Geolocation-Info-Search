package com.example.geolocationsearch.ui.theme.utils

import android.net.InetAddresses
import android.os.Build
import android.util.Patterns

/**
 * Checks if the given string is a valid IPv4 address.
 */
fun isValidIpAddress(ipAddress: String): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        InetAddresses.isNumericAddress(ipAddress)
    } else {
        @Suppress("DEPRECATION")
        Patterns.IP_ADDRESS.matcher(ipAddress).matches()
    }
}