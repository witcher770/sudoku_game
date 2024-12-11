package com.example.sudoku_game.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sudoku_game.models.SudokuViewModel
import com.example.sudoku_game.ui.components.Grid
import com.example.sudoku_game.ui.components.SudokuCell
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    sudokuViewModel: SudokuViewModel = viewModel()
) {
    var selectedNumber by remember { mutableStateOf<Int?>(null) }
    var eraseMode by remember { mutableStateOf(false) }
//    val sudokuGrid = remember { mutableStateOf(generateEmptySudokuGrid()) }
    val sudokuGrid = remember { mutableStateOf<Array<Array<SudokuCell>>?>(null) }

    LaunchedEffect(Unit) {
        val savedGrid = sudokuViewModel.loadLastGame()
        if (savedGrid != null) {
            sudokuGrid.value = savedGrid // Восстанавливаем состояние
        }
        else {
            generateSudokuGridAsync { generatedGrid ->
                // Обновите UI здесь
                sudokuGrid.value = generatedGrid
                sudokuViewModel.saveGameState(sudokuGrid.value) // Сохраняем новое поле
            }
        }
    }

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
                if (sudokuGrid.value == null) {
                    // Показываем индикатор загрузки, пока поле не инициализировано
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp)) // Расстояние между индикатором и текстом
                        Text(text = "Пожалуйста подождите, уровень создаётся...", style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    // Отображаем сетку судоку, если она уже сгенерирована
                    SudokuGrid(
                        grid = sudokuGrid.value!!,
                        selectedNumber = selectedNumber,
                        eraseMode = eraseMode,
                        onCellClick = { row, col ->
                            val grid = sudokuGrid.value
                            if (grid != null && !grid[row][col].fixed) {
                                grid[row][col] =
                                    SudokuCell(if (eraseMode) null else selectedNumber, false)
                                sudokuViewModel.saveGameState(grid) // Сохранение прогресса
                            }
                        }
                    )

                }
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
    grid: Array<Array<SudokuCell>>,
    selectedNumber: Int?,
    eraseMode: Boolean,
    onCellClick: (row: Int, col: Int) -> Unit
) {
    Column {
        for (row in grid.indices step 3) {
            Row {
                for (col in grid[row].indices step 3) {
                    SubGrid(
                        row = row,
                        col =  col,
                        grid = grid,
                        selectedNumber = selectedNumber,
                        eraseMode = eraseMode,
                        onCellClick = onCellClick
                    )
                }
            }
        }
    }
}

@Composable
private fun SubGrid(
    row: Int,
    col: Int,
    grid: Array<Array<SudokuCell>>,
    selectedNumber: Int?,
    eraseMode: Boolean,
    onCellClick: (row: Int, col: Int) -> Unit
) {
    Column(modifier = Modifier.border(BorderStroke(3.dp, Color.Black))) {
        for (ind_row in row until row + 3) {
            Row {
                for (ind_col in col until col + 3) {
                    val cell = grid[ind_row][ind_col]
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .border(1.dp, Color.Black)
                            .clickable { onCellClick(ind_row, ind_col) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cell.number?.toString() ?: "",
                            color = if (cell.fixed) Color.Gray else Color.Black
                        )
                    }
                }
            }
        }
    }
}

fun generateSudokuGrid(): Array<Array<SudokuCell>> {
    val example = Grid()
    example.mix()
    example.generatePuzzle()
    val intArrayGrid: Array<IntArray> = example.get_grid()

    val nullableGrid: Array<Array<SudokuCell>> = Array(example.get_n() * example.get_n()) { row ->
        Array(example.get_n() * example.get_n()) { col ->
            val num = intArrayGrid[row][col]
            SudokuCell(number = num.takeIf { it != 0 }, fixed = num != 0) // Преобразуем 0 в null, если нужно
        }
    }
    return nullableGrid
}

fun generateSudokuGridAsync(onGridGenerated: (Array<Array<SudokuCell>>) -> Unit) {
    CoroutineScope(Dispatchers.Default).launch {
        val grid = generateSudokuGrid()
        withContext(Dispatchers.Main) {
            onGridGenerated(grid) // Возвращаем результат в UI-поток
        }
    }
}


fun generateEmptySudokuGrid(): Array<Array<SudokuCell>> {
    val example = Grid()
    val intArrayGrid: Array<IntArray> = example.get_grid()

    val nullableGrid: Array<Array<SudokuCell>> = Array(example.get_n() * example.get_n()) { row ->
        Array(example.get_n() * example.get_n()) { col ->
            val num = intArrayGrid[row][col]
            SudokuCell(number = num.takeIf { it != 0 }, fixed = num != 0) // Преобразуем 0 в null, если нужно
        }
    }
    return nullableGrid
}
