package co.feip.fefu2025.presentation.animeDetails

import androidx.lifecycle.ViewModel
import co.feip.fefu2025.data.repository.MockAnimeRepository
import co.feip.fefu2025.domain.model.Anime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AnimeDetailsViewModel : ViewModel() {
    private val _anime = MutableStateFlow<Anime?>(null)
    val anime: StateFlow<Anime?> = _anime

    fun loadAnimeById(id: Int) {
        _anime.value = MockAnimeRepository.getAnimeById(id)
    }
}
