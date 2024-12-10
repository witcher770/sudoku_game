package com.example.sudoku_game.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sudoku_game.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
//    navController: NavController,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var selectedNumber by remember { mutableStateOf<Int?>(null) }
    var eraseMode by remember { mutableStateOf(false) }
    val sudokuGrid = remember { mutableStateOf(generateEmptySudokuGrid()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Судоку") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Настройки")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Поле Судоку
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                SudokuGrid(
                    grid = sudokuGrid.value,
                    selectedNumber = selectedNumber,
                    eraseMode = eraseMode,
                    onCellClick = { row, col ->
                        if (eraseMode) {
                            sudokuGrid.value[row][col] = null
                        } else if (selectedNumber != null) {
                            sudokuGrid.value[row][col] = selectedNumber
                        }
                    }
                )
            }

            // Нижние кнопки
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { /* Логика отмены действия */ }) {
                    Text("Отмена")
                }
                Button(onClick = { eraseMode = !eraseMode }) {
                    Text(if (eraseMode) "Ластик: Вкл" else "Ластик: Выкл")
                }
                Button(onClick = { /* Логика подсказки */ }) {
                    Text("Совет")
                }
            }

            // Ряд цифр
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                (1..9).forEach { number ->
                    Button(
                        onClick = {
                            selectedNumber = number
                            eraseMode = false // Отключить ластик при выборе цифры
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Text(text = number.toString())
                    }
                }
            }
        }
    }
}

@Composable
fun SudokuGrid(
    grid: Array<Array<Int?>>,
    selectedNumber: Int?,
    eraseMode: Boolean,
    onCellClick: (row: Int, col: Int) -> Unit
) {
    Column {
        for (row in grid.indices) {
            Row {
                for (col in grid[row].indices) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .border(1.dp, Color.Black)
                            .clickable { onCellClick(row, col) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = grid[row][col]?.toString() ?: "",
//                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
        }
    }
}

fun generateEmptySudokuGrid(): Array<Array<Int?>> {
    return Array(9) { Array(9) { null } }
}
