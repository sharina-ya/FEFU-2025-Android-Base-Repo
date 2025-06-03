package co.feip.fefu2025.presentation.animeList

import co.feip.fefu2025.domain.usecase.GetAnimeListUseCase


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.feip.fefu2025.domain.model.Anime

class AnimeListViewModel(
    private val getAnimeListUseCase: GetAnimeListUseCase = GetAnimeListUseCase()
) : ViewModel() {

    private val _animeList = MutableLiveData<List<Anime>>()
    val animeList: LiveData<List<Anime>> = _animeList

    init {
        loadAnimeList()
    }

    private fun loadAnimeList() {
        val list = getAnimeListUseCase.execute()
        _animeList.value = list
    }
}
