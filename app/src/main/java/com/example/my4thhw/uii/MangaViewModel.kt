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
import com.example.my4thhw.model.MangaDetails
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
    private var detailsJob: Job? = null

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
            } catch (e: CancellationException) {
                throw e
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
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isError = true,
                    items = emptyList()
                )
            }
        }
    }

    fun toggleShowFavourites() {
        _uiState.value = _uiState.value.copy(
            showFavourites = !_uiState.value.showFavourites
        )
    }
    fun onFavouriteClick(manga: Manga) {
        viewModelScope.launch {
            try {
                repository.toggleFavourite(manga)
                loadFavourites()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
            }
        }
    }
    fun onFavouriteToggle(manga: MangaDetails) {
        viewModelScope.launch {
            try {
                repository.toggleFavourite(manga)
                loadFavourites()
                val isFav = repository.isFavourite(manga.id)
                val current = _detailsState.value
                if (current is MangaDetailsUiState.Success) {
                    _detailsState.value = current.copy(isFavourite = isFav)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
            }
        }
    }

    fun loadDetails(id: Int) {
        detailsJob?.cancel()
        detailsJob = viewModelScope.launch {
            _detailsState.value = MangaDetailsUiState.Loading
            try {
                val result = repository.getDetails(id)
                val isFav = repository.isFavourite(id)
                _detailsState.value = MangaDetailsUiState.Success(result, isFav)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val cached = repository.getDetailsFromCache(id)
                if (cached != null) {
                    _detailsState.value = MangaDetailsUiState.Success(cached, isFavourite = true)
                } else {
                    _detailsState.value = MangaDetailsUiState.Error("Ошибка загрузки")
                }
            }
        }
    }
}