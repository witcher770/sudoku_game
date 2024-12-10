package com.example.sudoku_game.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sudoku_game.ui.screens.GameScreen
import com.example.sudoku_game.ui.screens.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(
                onSettingsClick = { navController.navigate("settings") },
                onHistoryClick = { navController.navigate("history") },
//                onNewGameClick = { navController.navigate("new_game") },
                onNewGameClick = { navController.navigate("game_screen") },
                onTutorialClick = { navController.navigate("tutorial") }
            )
        }
        composable("game_screen") {
            GameScreen(
                onBackClick = { navController.navigate("main_screen") },
                onSettingsClick = { navController.navigate("game_settings") }
            )
        }
        // Другие маршруты...
    }
}
