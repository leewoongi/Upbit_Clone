package com.woon.domain.event.provider

interface NetworkStateProvider {
    val isAirplaneModeOn: Boolean
    val isWifiConnected: Boolean
    val isMobileDataConnected: Boolean
    val isNetworkAvailable: Boolean

    /** wifi, mobile, offline 중 하나 */
    val networkType: String

    fun getNetworkStateDescription(): String
}
