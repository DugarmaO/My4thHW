package com.example.my4thhw.uii

import com.example.my4thhw.model.Manga

data class MangaListUiState(
    val isLoading: Boolean = false,
    val items: List<Manga> = emptyList(),
    val favouriteList: List<Manga> = emptyList(),
    val query: String = "",
    val isError: Boolean = false,
    val showFavourites: Boolean = false,
)