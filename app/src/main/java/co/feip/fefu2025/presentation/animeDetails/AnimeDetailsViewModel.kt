package co.feip.fefu2025.presentation.animeDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.feip.fefu2025.data.repository.MockAnimeRepository
import co.feip.fefu2025.domain.model.Anime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnimeDetailsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<Anime?>>(UiState.Loading)
    val uiState: StateFlow<UiState<Anime?>> = _uiState

    fun loadAnimeById(id: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val anime = MockAnimeRepository.getAnimeById(id)
                if (anime != null) {
                    _uiState.value = UiState.Success(anime)
                } else {
                    _uiState.value = UiState.Error("Аниме не найдено")
                }
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

