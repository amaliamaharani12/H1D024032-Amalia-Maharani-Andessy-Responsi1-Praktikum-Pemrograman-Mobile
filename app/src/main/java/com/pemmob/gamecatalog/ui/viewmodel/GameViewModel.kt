package com.pemmob.gamecatalog.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.gamecatalog.data.model.Game
import com.pemmob.gamecatalog.data.model.GameDetail
import com.pemmob.gamecatalog.data.repository.GameRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

@OptIn(FlowPreview::class)
class GameViewModel(
    private val repository: GameRepository = GameRepository()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _gamesState = MutableStateFlow<UiState<List<Game>>>(UiState.Loading)
    val gamesState: StateFlow<UiState<List<Game>>> = _gamesState.asStateFlow()

    private val _detailState = MutableStateFlow<UiState<GameDetail>>(UiState.Loading)
    val detailState: StateFlow<UiState<GameDetail>> = _detailState.asStateFlow()

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce { query -> if (query.isBlank()) 0L else 500L }
                .distinctUntilChanged()
                .collectLatest { query -> loadGames(query.trim()) }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun retry() {
        viewModelScope.launch { loadGames(_searchQuery.value.trim()) }
    }

    private suspend fun loadGames(query: String) {
        _gamesState.value = UiState.Loading
        val result: UiState<List<Game>> = try {
            UiState.Success(repository.getGames(query))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            UiState.Error("Failed to load data: ${e.localizedMessage}")
        }
        _gamesState.value = result
    }

    fun loadGameDetail(id: Int) {
        viewModelScope.launch {
            _detailState.value = UiState.Loading
            val result: UiState<GameDetail> = try {
                UiState.Success(repository.getGameDetail(id))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                UiState.Error("Failed to load details: ${e.localizedMessage}")
            }
            _detailState.value = result
        }
    }
}