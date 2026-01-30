package com.woon.chart.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import com.woon.domain.breadcrumb.recorder.BreadcrumbRecorder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SystemEventReceiver @Inject constructor(
    private val breadcrumbRecorder: BreadcrumbRecorder
) {
    private var airplaneModeReceiver: BroadcastReceiver? = null
    private var wifiStateReceiver: BroadcastReceiver? = null
    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    fun register(context: Context) {
        registerAirplaneModeReceiver(context)
        registerWifiStateReceiver(context)
        registerNetworkCallback(context)
    }

    fun unregister(context: Context) {
        airplaneModeReceiver?.let {
            runCatching { context.unregisterReceiver(it) }
        }
        wifiStateReceiver?.let {
            runCatching { context.unregisterReceiver(it) }
        }
        networkCallback?.let {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            runCatching { cm.unregisterNetworkCallback(it) }
        }
    }

    private fun registerAirplaneModeReceiver(context: Context) {
        airplaneModeReceiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent) {
                val isOn = Settings.Global.getInt(
                    ctx.contentResolver,
                    Settings.Global.AIRPLANE_MODE_ON,
                    0
                ) != 0

                breadcrumbRecorder.recordSystem(
                    name = "AirplaneModeChange",
                    attrs = mapOf("enabled" to isOn.toString())
                )
            }
        }

        val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        context.registerReceiver(airplaneModeReceiver, filter)
    }

    private fun registerWifiStateReceiver(context: Context) {
        wifiStateReceiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context, intent: Intent) {
                val state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)
                val stateName = when (state) {
                    WifiManager.WIFI_STATE_ENABLED -> "enabled"
                    WifiManager.WIFI_STATE_DISABLED -> "disabled"
                    WifiManager.WIFI_STATE_ENABLING -> "enabling"
                    WifiManager.WIFI_STATE_DISABLING -> "disabling"
                    else -> "unknown"
                }

                // enabling/disabling은 중간 상태라 스킵
                if (stateName == "enabled" || stateName == "disabled") {
                    breadcrumbRecorder.recordSystem(
                        name = "WifiStateChange",
                        attrs = mapOf("state" to stateName)
                    )
                }
            }
        }

        val filter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        context.registerReceiver(wifiStateReceiver, filter)
    }

    private fun registerNetworkCallback(context: Context) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val capabilities = cm.getNetworkCapabilities(network)
                val type = when {
                    capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> "wifi"
                    capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> "mobile"
                    else -> "other"
                }

                breadcrumbRecorder.recordSystem(
                    name = "NetworkConnected",
                    attrs = mapOf("type" to type)
                )
            }

            override fun onLost(network: Network) {
                breadcrumbRecorder.recordSystem(
                    name = "NetworkLost",
                    attrs = emptyMap()
                )
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        cm.registerNetworkCallback(request, networkCallback!!)
    }
}
