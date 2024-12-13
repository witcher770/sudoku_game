package com.example.sudoku_game.models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class GameSettingsViewModel: ViewModel() {
    var isAutoCheckEnabled = mutableStateOf(false)
        private set

    fun toggleAutoCheck() {
        isAutoCheckEnabled.value = !isAutoCheckEnabled.value
    }
}