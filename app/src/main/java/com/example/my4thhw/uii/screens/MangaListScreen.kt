package com.example.my4thhw.uii.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.my4thhw.uii.MangaListUiState
import com.example.my4thhw.uii.widgets.MangaCard
import com.example.my4thhw.model.Manga
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaListScreen(
    ui: MangaListUiState,
    onSearchChange: (String) -> Unit,
    onItemClick: (Int) -> Unit,
    onFavoriteClick: (Manga) -> Unit,
    onRetry: () -> Unit,
    onToggleFavourites: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (ui.showFavourites) "Избранное" else "Manga") },
                actions = {
                    IconButton(onClick = onToggleFavourites) {
                        Icon(
                            imageVector = if (ui.showFavourites)
                                Icons.Filled.Favorite
                            else
                                Icons.Outlined.FavoriteBorder,
                            contentDescription = "Показать избранное"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            if (!ui.showFavourites) {
                OutlinedTextField(
                    value = ui.query,
                    onValueChange = onSearchChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Search") }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (ui.showFavourites) {
                if (ui.favouriteList.isEmpty()) {
                    Text("Нет сохранённой манги")
                } else {
                    LazyColumn {
                        items(
                            items = ui.favouriteList,
                            key = { it.id }
                        ) { manga ->
                            MangaCard(
                                manga = manga,
                                onClick = { onItemClick(manga.id) },
                                onFavoriteClick = { onFavoriteClick(manga) }
                            )
                        }
                    }
                }
            } else {
                when {
                    ui.isLoading -> {
                        CircularProgressIndicator()
                    }

                    ui.isError -> {
                        Column {
                            Text("Ошибка загрузки")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = onRetry) {
                                Text("Retry")
                            }
                        }
                    }

                    ui.items.isEmpty() -> {
                        Text("Ничего не найдено")
                    }
                    else -> {
                        LazyColumn {
                            items(
                                items = ui.items,
                                key = { it.id }
                            ) { manga ->
                                MangaCard(
                                    manga = manga,
                                    onClick = { onItemClick(manga.id) },
                                    onFavoriteClick = { onFavoriteClick(manga) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
