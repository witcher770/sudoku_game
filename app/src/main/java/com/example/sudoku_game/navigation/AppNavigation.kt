package com.example.sudoku_game.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sudoku_game.models.SudokuViewModel
import com.example.sudoku_game.ui.screens.GameScreen
import com.example.sudoku_game.ui.screens.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val context = LocalContext.current.applicationContext as Application
    if (context !is Application) {
        throw IllegalStateException("Context is not an Application context")
    }
    val sudokuViewModel = SudokuViewModel(context)

    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(
                onSettingsClick = { navController.navigate("settings") },
                onHistoryClick = { navController.navigate("history") },
//                onNewGameClick = { navController.navigate("new_game") },
                onNewGameClick = { navController.navigate("game_screen") },
                onLastGameClick = { navController.navigate("game_screen") },
                onTutorialClick = { navController.navigate("tutorial") }
            )
        }
        composable("game_screen") {
            GameScreen(
                onBackClick = { navController.navigate("main_screen") },
                onSettingsClick = { navController.navigate("game_settings") },
                sudokuViewModel
            )
        }
        // Другие маршруты...
    }
}
