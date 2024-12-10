package com.example.sudoku_game.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sudoku_game.R
import com.example.sudoku_game.data.DatabaseInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onSettingsClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onNewGameClick: () -> Unit,
    onLastGameClick: () -> Unit,
    onTutorialClick: () -> Unit
) {
    var hasSavedGame by remember { mutableStateOf(false) }
    val context = LocalContext.current // Получение контекста


    LaunchedEffect(Unit) {
        val lastGame = withContext(Dispatchers.IO) {
            DatabaseInstance.getDatabase(context).sudokuDao().getLastGame()
        }
        hasSavedGame = lastGame != null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Судоку") },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Настройки")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Логотип
                Image(
                    painter = painterResource(id = R.drawable.my_logo), // Укажите ваш логотип
                    contentDescription = "Логотип",
                    modifier = Modifier.size(150.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Кнопки
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (hasSavedGame) {
                        Button(onClick = onLastGameClick,
                               modifier = Modifier
                                   .fillMaxWidth(0.7f)
                                   .padding(vertical = 8.dp)) {
                            Text("Продолжить игру")
                        }
                    }

                    Button(
                        onClick = onHistoryClick,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(vertical = 8.dp)
                    ) {
                        Text("История игр")
                    }

                    Button(
                        onClick = onNewGameClick,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Новая игра")
                    }

                    Button(
                        onClick = onTutorialClick,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Обучение")
                    }
                }
            }
        }
    }
}
