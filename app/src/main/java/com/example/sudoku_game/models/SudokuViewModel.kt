package com.example.sudoku_game.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.sudoku_game.data.DatabaseInstance
import com.example.sudoku_game.data.SudokuDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.gson.Gson

class SudokuViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = DatabaseInstance.getDatabase(application).sudokuDao()


    suspend fun loadLastGame(): Array<Array<Int?>>? {
        val lastGame = dao.getLastGame() ?: return null
        return Gson().fromJson(lastGame.gridState, Array<Array<Int?>>::class.java)
    }

    fun saveGameState(grid: Array<Array<Int?>>) {
//        val dao = DatabaseInstance.getDatabase(context).sudokuDao()
        val gridStateJson = Gson().toJson(grid)
        CoroutineScope(Dispatchers.IO).launch {
            dao.saveGame(SudokuDataModel(gridState = gridStateJson))
        }
    }
}