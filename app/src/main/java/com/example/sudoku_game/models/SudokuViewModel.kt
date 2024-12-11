package com.example.sudoku_game.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sudoku_game.data.DatabaseInstance
import com.example.sudoku_game.ui.components.SudokuCell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.gson.Gson

class SudokuViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = DatabaseInstance.getDatabase(application).sudokuDao()


    suspend fun loadLastGame(): Array<Array<SudokuCell>>? {
        val lastGame = dao.getLastGame() ?: return null
        return Gson().fromJson(lastGame.gridState, Array<Array<SudokuCell>>::class.java)
    }

    fun saveGameState(grid: Array<Array<SudokuCell>>?) {
//        val dao = DatabaseInstance.getDatabase(context).sudokuDao()
        val gridStateJson = Gson().toJson(grid)
        CoroutineScope(Dispatchers.IO).launch {
            dao.saveGame(SudokuDataModel(gridState = gridStateJson))
        }
    }
    fun clearGameHistory() {
        CoroutineScope(Dispatchers.IO).launch {
            dao.clearAllGames()
        }
    }
}