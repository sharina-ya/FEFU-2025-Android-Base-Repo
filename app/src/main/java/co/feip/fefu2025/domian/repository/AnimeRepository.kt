package co.feip.fefu2025.domain.repository
import co.feip.fefu2025.domain.model.Anime

interface AnimeRepository {
    suspend fun getAnimeList(): List<Anime>
    suspend fun getAnimeById(id: Int): Anime?
    suspend fun getPopularAnime(page: Int = 1): List<Anime>
    suspend fun getAnimeDetailsById(id: Int): Anime?
    suspend fun getRecommendations(animeId: Int, page: Int, pageSize: Int = 10): List<Anime>
    suspend fun searchAnime(query: String, page: Int, pageSize: Int = 20): List<Anime>
}