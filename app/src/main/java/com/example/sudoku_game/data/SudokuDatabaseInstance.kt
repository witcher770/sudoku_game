package com.example.sudoku_game.data

import android.content.Context
import androidx.room.Room

object DatabaseInstance {
    private var db: SudokuDatabase? = null

    fun getDatabase(context: Context): SudokuDatabase {
        if (db == null) {
            db = Room.databaseBuilder(
                context.applicationContext,
                SudokuDatabase::class.java,
                "sudoku_database"
            ).build()
        }
        return db!!
    }
}
