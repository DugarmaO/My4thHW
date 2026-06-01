package com.example.my4thhw.uii

import com.example.my4thhw.model.MangaDetails
sealed class MangaDetailsUiState {
    object Loading : MangaDetailsUiState()
    data class Success(
        val manga: MangaDetails
    ) : MangaDetailsUiState()
    data class Error(
        val message: String
    ) : MangaDetailsUiState()
}