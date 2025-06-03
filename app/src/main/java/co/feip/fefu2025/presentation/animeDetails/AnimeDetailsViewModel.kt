package co.feip.fefu2025.presentation.animeDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.usecase.GetAnimeDetailsUseCase

class AnimeDetailsViewModel(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase = GetAnimeDetailsUseCase()
) : ViewModel() {

    private val _anime = MutableLiveData<Anime>()
    val anime: LiveData<Anime> = _anime

    fun loadAnimeDetails(anime: Anime) {
        val details = getAnimeDetailsUseCase.execute(anime)
        _anime.value = details
    }
}
