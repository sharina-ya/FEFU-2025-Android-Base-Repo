package co.feip.fefu2025.presentation.animeList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.feip.fefu2025.data.repository.MockAnimeRepository
import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnimeListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<Anime>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Anime>>> = _uiState

    private val _popularUiState = MutableStateFlow<UiState<List<Anime>>>(UiState.Success(emptyList()))
    val popularUiState: StateFlow<UiState<List<Anime>>> = _popularUiState

    private var currentPage = 1
    private var isLoadingPage = false
    private var isLastPage = false

    init {
        loadAnimeList()
        loadPopularAnimePage(1)
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

    fun loadPopularAnimePage(page: Int) {
        if (isLoadingPage || isLastPage) return
        isLoadingPage = true

        viewModelScope.launch {
            _popularUiState.value = UiState.Loading
            try {
                val popularList = MockAnimeRepository.getPopularAnime(page)
                if (popularList.isEmpty()) {
                    isLastPage = true
                } else {
                    val currentList = (_popularUiState.value as? UiState.Success)?.data ?: emptyList()
                    _popularUiState.value = UiState.Success(currentList + popularList)
                    currentPage = page
                }
            } catch (e: Exception) {
                _popularUiState.value = UiState.Error(e.message ?: "Ошибка загрузки популярных аниме")
            } finally {
                isLoadingPage = false
            }
        }
    }

    fun loadNextPopularPage() {
        if (!isLoadingPage && !isLastPage) {
            loadPopularAnimePage(currentPage + 1)
        }
    }
}



class RecommendationsViewModel(
    private val repository: AnimeRepository = MockAnimeRepository // Используем MockAnimeRepository по умолчанию
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Anime>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Anime>>> = _uiState

    init {
        loadAllAnime() // Загружаем все аниме при инициализации ViewModel
    }

    private fun loadAllAnime() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading // Показываем загрузку
            try {
                val allAnimeList = repository.getAnimeList() // Вызываем getAnimeList()
                _uiState.value = UiState.Success(allAnimeList) // Обновляем состояние успешной загрузкой
            } catch (e: Exception) {
                // В случае ошибки показываем сообщение об ошибке
                _uiState.value = UiState.Error(e.message ?: "Ошибка загрузки всех аниме")
            }
        }
    }
}



sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
