package com.example.my4thhw.uii.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.my4thhw.uii.MangaDetailsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailsScreen(
    ui: MangaDetailsUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Details") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Button(onClick = onBack) {
                Text("Back")
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (ui) {

                is MangaDetailsUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is MangaDetailsUiState.Error -> {
                    Column {
                        Text(ui.message)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRetry) {
                            Text("Retry")
                        }
                    }
                }
                is MangaDetailsUiState.Success -> {
                    val m = ui.manga
                    Text(m.title, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Жанр: ${m.genre}")
                    Text("Рейтинг: ${m.rating?.toString() ?: "Нет данных"}")
                    Text("Автор: ${m.author}")
                    Text("Главы: ${m.chapters?.toString() ?: "Нет данных"}")
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(m.description)
                }
            }
        }
    }
}