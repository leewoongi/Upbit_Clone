package com.woon.main.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.woon.HomeScreen
import com.woon.detail.DetailScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreen()
        }

        composable(
            route = "detail/{marketCode}",
            arguments = listOf(
                navArgument("marketCode") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            DetailScreen()
        }
    }
}