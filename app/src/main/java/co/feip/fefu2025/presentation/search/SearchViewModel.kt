package co.feip.fefu2025.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.feip.fefu2025.data.repository.MockAnimeRepository
import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.repository.AnimeRepository
import co.feip.fefu2025.presentation.animeList.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: AnimeRepository = MockAnimeRepository // Используйте ваш репозиторий
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Anime>>>(UiState.Success(emptyList()))
    val uiState: StateFlow<UiState<List<Anime>>> = _uiState

    var isLoading = false
        private set

    private var currentPage = 1
    private val pageSize = 20
    private var currentQuery: String = ""
    private var isLastPage = false

    /**
     * Запускает новый поиск по запросу.
     * Если запрос совпадает с текущим, повторный поиск не запускается.
     */
    fun searchAnime(query: String) {
        if (query == currentQuery) return
        currentQuery = query
        currentPage = 1
        isLastPage = false
        loadPage(reset = true)
    }

    /**
     * Загружает следующую страницу результатов, если это возможно.
     */
    fun loadNextPage() {
        if (isLoading || isLastPage || currentQuery.isBlank()) return
        loadPage(reset = false)
    }

    /**
     * Загружает страницу результатов поиска.
     * @param reset - если true, сбрасывает текущие результаты и показывает индикатор загрузки.
     */
    private fun loadPage(reset: Boolean) {
        viewModelScope.launch {
            isLoading = true
            if (reset) {
                _uiState.value = UiState.Loading
            }
            try {
                val results = repository.searchAnime(currentQuery, currentPage, pageSize)
                if (results.isEmpty()) {
                    isLastPage = true
                }
                val currentList = if (reset) emptyList() else (_uiState.value as? UiState.Success)?.data ?: emptyList()
                _uiState.value = UiState.Success(currentList + results)
                currentPage++
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Ошибка поиска")
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Сбрасывает состояние поиска и очищает результаты.
     */
    fun reset() {
        currentQuery = ""
        currentPage = 1
        isLastPage = false
        _uiState.value = UiState.Success(emptyList())
    }
}
