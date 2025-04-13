package com.example.turingmachineapplication.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.turingmachineapplication.features.TmProcess.presentation.TmProcessPage
import com.example.turingmachineapplication.features.TmInput.presentation.TmInputPage

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: Screen
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ){
        composable(route = Screen.TmInput.route) {
            TmInputPage(navController)
        }

        composable(
            route = Screen.TmProcess.route,
            arguments = listOf(
                navArgument("algorithm") { NavType.StringType},
                navArgument("tape") { NavType.StringType}
            )) {
                TmProcessPage(navController)
            }
    }
}