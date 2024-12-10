package com.example.sudoku_game.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sudoku_game.models.SudokuDataModel

@Database(entities = [SudokuDataModel::class], version = 1)
abstract class SudokuDatabase : RoomDatabase() {
    abstract fun sudokuDao(): SudokuDao
}
