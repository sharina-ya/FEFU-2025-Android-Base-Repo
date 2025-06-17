package co.feip.fefu2025.domain.repository

import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.R

object AnimeRepository {

    private val animeList = listOf(
        Anime(1, "Наруто", "Описание Наруто", listOf("Экшен", "Приключения", "Комедия"), 8.5, 2002, 220, R.drawable.naruto, mapOf(1 to 50)),
        Anime(2, "Маг целитель", "Описание Маг целитель", listOf("Романтика", "Драма", "Комедия"), 9.0, 2022, 12, R.drawable.aot, mapOf(1 to 20)),
        Anime(3, "Blue lock", "Описание Blue lock", listOf("Экшен", "Спорт", "Комедия"), 8.7, 2022, 24, R.drawable.bluelock, mapOf(1 to 10)),
        Anime(4, "Я переродился торговым автоматом", "Описание...", listOf("Исэкай", "Фэнтези", "Комедия"), 4.1, 2024, 12, R.drawable.chto, mapOf(1 to 10))
    )

    fun getAnimeById(id: Int): Anime? = animeList.find { it.id == id }
}
