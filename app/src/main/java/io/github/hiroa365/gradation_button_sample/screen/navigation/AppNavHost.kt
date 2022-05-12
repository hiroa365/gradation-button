package io.github.hiroa365.gradation_button_sample.screen.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.hiroa365.gradation_button_sample.screen.main.MainScreen
import io.github.hiroa365.gradation_button_sample.screen.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Main.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Main.route) {
            MainScreen(
                navigateToSettings = { navController.navigate(Screen.Settings.route) },
            )
        }

        composable(route = Screen.Settings.route) {
            SettingsScreen(
                navigateToMain = { navController.navigate(Screen.Main.route) },
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Main : Screen(route = "main")
    object Settings : Screen(route = "settings")
}