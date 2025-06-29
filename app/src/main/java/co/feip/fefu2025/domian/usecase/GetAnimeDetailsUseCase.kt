package co.feip.fefu2025.domain.usecase

import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.domain.repository.AnimeRepository

class GetAnimeDetailsUseCase(private val repository: AnimeRepository) {
    suspend fun execute(anime: Anime): Anime? {
        return repository.getAnimeById(anime.id)
    }
}
