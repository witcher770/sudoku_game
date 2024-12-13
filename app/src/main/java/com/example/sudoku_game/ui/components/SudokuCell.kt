package com.example.sudoku_game.ui.components

import androidx.compose.runtime.MutableState

data class SudokuCell(
    var number: Int?,
//    var number: MutableState<Int?>,
    val fixed: Boolean
)