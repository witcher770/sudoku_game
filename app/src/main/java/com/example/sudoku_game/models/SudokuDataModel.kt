package com.example.sudoku_game.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sudoku_games")
data class SudokuDataModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gridState: String, // Сохраняем матрицу как JSON
    val lastModified: Long = System.currentTimeMillis() // Для сортировки по времени
)
