package com.woon.repository.event

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import com.woon.domain.event.provider.NetworkStateProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkStateProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NetworkStateProvider {

    private val connectivityManager: ConnectivityManager?
        get() = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    override val isAirplaneModeOn: Boolean
        get() = Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON,
            0
        ) != 0

    override val isWifiConnected: Boolean
        get() = runCatching {
            val cm = connectivityManager ?: return@runCatching false
            val network = cm.activeNetwork ?: return@runCatching false
            val capabilities = cm.getNetworkCapabilities(network) ?: return@runCatching false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }.getOrDefault(false)

    override val isMobileDataConnected: Boolean
        get() = runCatching {
            val cm = connectivityManager ?: return@runCatching false
            val network = cm.activeNetwork ?: return@runCatching false
            val capabilities = cm.getNetworkCapabilities(network) ?: return@runCatching false
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }.getOrDefault(false)

    override val isNetworkAvailable: Boolean
        get() = runCatching {
            val cm = connectivityManager ?: return@runCatching false
            val network = cm.activeNetwork ?: return@runCatching false
            val capabilities = cm.getNetworkCapabilities(network) ?: return@runCatching false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }.getOrDefault(false)

    override val networkType: String
        get() = runCatching {
            when {
                isWifiConnected -> "wifi"
                isMobileDataConnected -> "mobile"
                else -> "offline"
            }
        }.getOrDefault("unknown")

    override fun getNetworkStateDescription(): String {
        return buildString {
            append("airplane=")
            append(if (isAirplaneModeOn) "ON" else "OFF")
            append(", wifi=")
            append(if (isWifiConnected) "ON" else "OFF")
            append(", mobile=")
            append(if (isMobileDataConnected) "ON" else "OFF")
            append(", available=")
            append(if (isNetworkAvailable) "YES" else "NO")
        }
    }
}
