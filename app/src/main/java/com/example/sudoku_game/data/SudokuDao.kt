package com.example.sudoku_game.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sudoku_game.models.SudokuDataModel

@Dao
interface SudokuDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGame(game: SudokuDataModel)

    @Query("SELECT * FROM sudoku_games ORDER BY lastModified DESC LIMIT 1")
    suspend fun getLastGame(): SudokuDataModel?

    @Query("DELETE FROM sudoku_games")
    suspend fun clearAllGames()
}
