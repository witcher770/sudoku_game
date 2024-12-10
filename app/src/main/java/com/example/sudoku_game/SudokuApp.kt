package com.example.sudoku_game

import android.app.Application
import com.example.sudoku_game.data.DatabaseInstance

class SudokuApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseInstance.getDatabase(this) // Инициализация базы данных
    }
}
