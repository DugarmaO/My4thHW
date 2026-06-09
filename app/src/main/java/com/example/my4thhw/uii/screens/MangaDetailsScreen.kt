package com.example.my4thhw.uii.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.my4thhw.uii.MangaDetailsUiState
import com.example.my4thhw.model.MangaDetails
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailsScreen(
    ui: MangaDetailsUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onFavoriteToggle: (MangaDetails) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    if (ui is MangaDetailsUiState.Success) {
                        IconButton(onClick = { onFavoriteToggle(ui.manga) }) {
                            Icon(
                                imageVector = if (ui.isFavourite)
                                    Icons.Filled.Favorite
                                else
                                    Icons.Outlined.FavoriteBorder,
                                contentDescription = "Избранное"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->

        when (ui) {
            is MangaDetailsUiState.Loading -> {
                Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                    CircularProgressIndicator()
                }
            }

            is MangaDetailsUiState.Error -> {
                Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                    Text(ui.message)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRetry) {
                        Text("Retry")
                    }
                }
            }

            is MangaDetailsUiState.Success -> {
                val m = ui.manga
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
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