package co.feip.fefu2025.data.repository

import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.R
import co.feip.fefu2025.data.repository.MockAnimeRepository.animeList
import co.feip.fefu2025.domain.repository.AnimeRepository
import kotlinx.coroutines.delay
import kotlin.random.Random
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.Path

object MockAnimeRepository : AnimeRepository {

    override suspend fun getAnimeDetailsById(id: Int): Anime? {
        return try {
            val response = JikanApi.service.getAnimeDetails(id)
            val dto = response.data

            // Найдём моковые данные по id или названию
            val mockAnime = animeList.find { it.id == id || it.title == dto.title }

            // Собираем жанры из API
            val genresFromApi = dto.genres?.map { it.name } ?: emptyList()

            Anime(
                id = dto.malId,
                title = dto.title,
                description = dto.synopsis ?: mockAnime?.description.orEmpty(),
                genres = if (genresFromApi.isNotEmpty()) genresFromApi else (mockAnime?.genres ?: emptyList()),
                rating = dto.score ?: mockAnime?.rating ?: 0.0,
                year = dto.year ?: mockAnime?.year ?: 0,
                episodes = dto.episodes ?: mockAnime?.episodes ?: 0,
                imageResId = mockAnime?.imageResId ?: 0,
                imageUrl = dto.images.jpg.imageUrl ?: mockAnime?.imageUrl.orEmpty(),
                ratingsDistribution = mockAnime?.ratingsDistribution ?: emptyMap()
            )
        } catch (e: Exception) {
            // В случае ошибки возвращаем моковые данные
            animeList.find { it.id == id }
        }
    }

    override suspend fun searchAnime(query: String, page: Int, pageSize: Int): List<Anime> {
        val popularList = getPopularAnime(1) // или загрузить все популярные, если есть кэш
        val filtered = popularList.filter { it.title.contains(query, ignoreCase = true) }
        val fromIndex = (page - 1) * pageSize
        if (fromIndex >= filtered.size) return emptyList()
        val toIndex = minOf(fromIndex + pageSize, filtered.size)
        return filtered.subList(fromIndex, toIndex)
    }



    val animeList = listOf(
        Anime(
            id = 1,
            title = "Наруто",
            description = "Главный герой — Наруто Узумаки...",
            genres = listOf("Экшен", "Приключения", "Комедия"),
            rating = 8.5,
            year = 2002,
            episodes = 220,
            imageResId = R.drawable.naruto,
            imageUrl = "", // Можно оставить пустым для локального изображения
            ratingsDistribution = mapOf(
                1 to 50, 2 to 30, 3 to 100, 4 to 200, 5 to 300,
                6 to 500, 7 to 800, 8 to 1200, 9 to 900, 10 to 600
            )
        ),
        Anime(
            id = 2,
            title = "Маг целитель",
            description = "Когда Кэяру получил способность исцелять...",
            genres = listOf("Романтика", "Драма", "Комедия"),
            rating = 9.0,
            year = 2022,
            episodes = 12,
            imageResId = R.drawable.aot,
            imageUrl = "",
            ratingsDistribution = mapOf(
                1 to 20, 2 to 15, 3 to 50, 4 to 100, 5 to 200,
                6 to 300, 7 to 500, 8 to 800, 9 to 600, 10 to 400
            )
        ),
    )

    private fun shouldThrowError(): Boolean = Random.nextFloat() < 0.3f

    override suspend fun getAnimeList(): List<Anime> {
        delay(1)
        if (shouldThrowError()) {
            throw Exception("Ошибка загрузки списка аниме")
        }
        return animeList
    }

    override suspend fun getAnimeById(id: Int): Anime? {
        delay(1)
        if (shouldThrowError()) {
            throw Exception("Ошибка загрузки деталей аниме")
        }
        return animeList.find { it.id == id }
    }



    override suspend fun getRecommendations(animeId: Int, page: Int, pageSize: Int): List<Anime> {
        val allRecommendations = animeList.filter { it.id != animeId }.shuffled()
        val fromIndex = (page - 1) * pageSize
        if (fromIndex >= allRecommendations.size) return emptyList()
        val toIndex = minOf(fromIndex + pageSize, allRecommendations.size)
        return allRecommendations.subList(fromIndex, toIndex)

    }

    override suspend fun getPopularAnime(page: Int): List<Anime> {
        val jikanList = JikanApi.service.getTopAnime(page).data
        return jikanList.map { dto ->
            Anime(
                id = dto.malId,
                title = dto.title,
                description = dto.synopsis ?: "",
                imageResId = 0,
                imageUrl = dto.images.jpg.imageUrl ?: "",
                genres = emptyList(),
                rating = dto.score ?: 0.0,
                year = 0,
                episodes = 0,
                ratingsDistribution = emptyMap()
            )
        }
    }

}

// Retrofit API и DTO остаются без изменений
interface JikanApiService {
    @GET("top/anime")
    suspend fun getTopAnime(@Query("page") page: Int = 1): JikanTopAnimeResponse
    @GET("anime/{id}/full")
    suspend fun getAnimeDetails(@Path("id") id: Int): JikanAnimeDetailsResponse
}

@JsonClass(generateAdapter = true)
data class JikanTopAnimeResponse(val data: List<JikanAnimeDto>)

@JsonClass(generateAdapter = true)
data class JikanAnimeDto(
    @Json(name = "mal_id") val malId: Int,
    val title: String,
    val synopsis: String?,
    val score: Double?,
    val images: JikanImagesDto
)

@JsonClass(generateAdapter = true)
data class JikanImagesDto(val jpg: JikanImageUrlDto)

@JsonClass(generateAdapter = true)
data class JikanImageUrlDto(@Json(name = "image_url") val imageUrl: String?)

object JikanApi {
    val service: JikanApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v4/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(JikanApiService::class.java)
    }
}
@JsonClass(generateAdapter = true)
data class JikanAnimeDetailsResponse(val data: JikanAnimeDetailsDto)

@JsonClass(generateAdapter = true)
data class JikanAnimeDetailsDto(
    @Json(name = "mal_id") val malId: Int,
    val title: String,
    val synopsis: String?,
    val score: Double?,
    val year: Int?,
    val episodes: Int?,
    val genres: List<JikanGenreDto>?,
    val images: JikanImagesDto
)

@JsonClass(generateAdapter = true)
data class JikanGenreDto(val name: String)

