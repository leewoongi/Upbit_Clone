package com.woon.main

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.rememberNavController
import com.woon.core.ui.design.theme.ChartAppTheme
import com.woon.core.ui.provides.LocalClickTracker
import com.woon.core.ui.provides.LocalNavController
import com.woon.core.ui.design.theme.theme.ChartTheme
import com.woon.domain.breadcrumb.recorder.BreadcrumbRecorder
import com.woon.main.ui.NavigationGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var breadcrumbRecorder: BreadcrumbRecorder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChartAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = ChartTheme.colors.background
                ) { innerPadding ->
                    val navController = rememberNavController()

                    // NAV breadcrumb 자동 기록
                    LaunchedEffect(navController) {
                        navController.addOnDestinationChangedListener { _, destination, arguments ->
                            val route = destination.route ?: return@addOnDestinationChangedListener
                            val resolvedRoute = resolveRoute(route, arguments)
                            val maskedArgs = maskArguments(arguments)

                            breadcrumbRecorder.recordNav(
                                route = route,
                                attrs = buildMap {
                                    put("to", resolvedRoute)
                                    if (maskedArgs.isNotEmpty()) {
                                        put("args", maskedArgs)
                                    }
                                }
                            )
                        }
                    }

                    CompositionLocalProvider(
                        LocalNavController provides navController,
                        LocalClickTracker provides { name, attrs ->
                            breadcrumbRecorder.recordClick(name, attrs)
                        }
                    ) {
                        NavigationGraph(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }

    private fun resolveRoute(route: String, arguments: Bundle?): String {
        if (arguments == null || arguments.isEmpty) return route
        var resolved = route
        arguments.keySet().forEach { key ->
            val value = arguments.getString(key) ?: arguments.get(key)?.toString() ?: return@forEach
            resolved = resolved.replace("{$key}", maskValue(value))
        }
        return resolved
    }

    private fun maskArguments(arguments: Bundle?): String {
        if (arguments == null || arguments.isEmpty) return ""
        return arguments.keySet()
            .mapNotNull { key ->
                val value = arguments.getString(key) ?: arguments.get(key)?.toString()
                value?.let { "$key=${maskValue(it)}" }
            }
            .joinToString(",")
    }

    private fun maskValue(value: String): String {
        if (value.length <= 20) return value
        return "${value.take(10)}...${value.takeLast(4)}"
    }
}
