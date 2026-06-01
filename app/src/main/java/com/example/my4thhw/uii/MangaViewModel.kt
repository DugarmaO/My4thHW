package com.example.my4thhw.uii

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my4thhw.data.MangaRepository
import com.example.my4thhw.model.Manga
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class MangaViewModel @Inject constructor(
    private val repository: MangaRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MangaListUiState())
    val uiState = _uiState.asStateFlow()

    private val _detailsState =
        MutableStateFlow<MangaDetailsUiState>(MangaDetailsUiState.Loading)
    val detailsState = _detailsState.asStateFlow()

    private var listJob: Job? = null

    init {
        loadFavourites()
        loadInitial()
    }

    private fun loadFavourites() {
        viewModelScope.launch {
            try {
                val favourites = repository.getFavourites()
                val favouriteIds = favourites.map { it.id }.toSet()

                _uiState.value = _uiState.value.copy(
                    favouriteList = favourites,
                    items = _uiState.value.items.map { manga ->
                        manga.copy(isFavourite = manga.id in favouriteIds)
                    }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isError = true)
            }
        }
    }


    fun loadInitial() {
        listJob?.cancel()
        listJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isError = false,
                items = emptyList()
            )
            try {
                val result = repository.getMangaList()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    items = result
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isError = true,
                    items = emptyList()
                )
            }
        }
    }

    fun onSearchChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)

        listJob?.cancel()

        if (query.isBlank()) {
            loadFavourites()
            loadInitial()
            return
        }

        listJob = viewModelScope.launch {
            delay(400)
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isError = false,
                items = emptyList()
            )
            try {
                val result = repository.searchManga(query)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    items = result
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isError = true,
                    items = emptyList()
                )
            }
        }
    }

    fun onFavouriteClick(manga: Manga) {
        viewModelScope.launch {
            try {
                repository.toggleFavourite(manga)

                val favourites = repository.getFavourites()
                val favouriteIds = favourites.map { it.id }.toSet()

                _uiState.value = _uiState.value.copy(
                    favouriteList = favourites,
                    items = uiState.value.items.map { current ->
                        current.copy(isFavourite = current.id in favouriteIds)
                    }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isError = true)
            }
        }
    }

    fun loadDetails(id: Int) {
        viewModelScope.launch {
            _detailsState.value = MangaDetailsUiState.Loading
            try {
                val result = repository.getDetails(id)
                _detailsState.value = MangaDetailsUiState.Success(result)
            } catch (e: Exception) {
                _detailsState.value = MangaDetailsUiState.Error("Ошибка загрузки")
            }
        }
    }
}