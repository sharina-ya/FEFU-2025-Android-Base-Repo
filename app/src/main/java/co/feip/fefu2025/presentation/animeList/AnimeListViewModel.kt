package co.feip.fefu2025.presentation.animeList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.feip.fefu2025.data.repository.MockAnimeRepository
import co.feip.fefu2025.domain.model.Anime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class AnimeListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<Anime>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Anime>>> = _uiState

    init {
        loadAnimeList()
    }

    fun loadAnimeList() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val list = MockAnimeRepository.getAnimeList()
                _uiState.value = UiState.Success(list)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }
}
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
