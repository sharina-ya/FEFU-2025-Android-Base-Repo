package co.feip.fefu2025

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import androidx.recyclerview.widget.LinearLayoutManager
import co.feip.fefu2025.databinding.ActivityAnimeDetailsBinding
import androidx.core.content.ContextCompat
import android.view.Gravity
import android.view.View

import android.widget.TextView

class AnimeDetailsActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAnimeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация данных аниме
        val anime = intent.getParcelableExtra<Anime>("anime") ?: run {
            finish()
            return
        }

        setupUI(anime)
        setupRatingChart(anime) // Передаем аниме с его рейтингами
        setupRecommendations()
    }

    private fun setupUI(anime: Anime) {
        // 1. Настройка основных данных
        with(binding) {
            returnBtn.setOnClickListener { finish() }
            animeImage.setImageResource(anime.imageResId)
            animeTitle.text = anime.title
            animeRating.text = anime.rating.toString()
            animeYear.text = anime.year.toString()
            animeEpisodes.text = "${anime.episodes} эп."
            animeDescription.text = anime.description

            // Очистка и добавление жанров
            genresContainer.removeAllViews()
            anime.genres.forEach { genre ->
                AnimeGenreView(this@AnimeDetailsActivity).apply {
                    setGenre(genre, getRandomColor())
                    genresContainer.addView(this)
                }
            }
        }

        // 2. Настройка графика рейтинга
        setupRatingChart(anime)

        // 3. Настройка рекомендаций
        setupRecommendations()
    }

    private fun setupRatingChart(anime: Anime) {
        val ratings = anime.ratingsDistribution.ifEmpty {
            // Дефолтные значения, если рейтинги не заданы
            mapOf(
                1 to 100, 2 to 50, 3 to 200, 4 to 300, 5 to 450,
                6 to 600, 7 to 800, 8 to 700, 9 to 400, 10 to 500
            )
        }

        val maxCount = ratings.values.maxOrNull() ?: 1
        val chartContainer = binding.ratingChartContainer
        val labelsContainer = binding.ratingLabelsContainer

        chartContainer.removeAllViews()
        labelsContainer.removeAllViews()

        ratings.entries.sortedBy { it.key }.forEach { (rating, count) ->
            // Столбец диаграммы
            View(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    (count.toFloat() / maxCount * 150).toInt(),
                    1f
                ).apply { setMargins(2) }
                setBackgroundColor(ContextCompat.getColor(this@AnimeDetailsActivity, R.color.blue))
                chartContainer.addView(this)
            }

            // Подпись
            TextView(this).apply {
                text = rating.toString()
                gravity = Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                textSize = 12f
                labelsContainer.addView(this)
            }
        }
    }

    private fun setupRecommendations() {
        // Создаем тестовые данные (замените на реальные)
        val recommendations = listOf(
            Anime(1, "Хуевое аниме", "хуйня", listOf("Боевик"), 9.0, 1999, 12, R.drawable.aot, ratingsDistribution = mapOf(
                1 to 10, 2 to 15, 3 to 30, 4 to 50, 5 to 100,
                6 to 300, 7 to 600, 8 to 900, 9 to 1200, 10 to 1500
            )),
            Anime(1, "Хуевое аниме", "хуйня", listOf("Боевик"), 9.0, 1999, 12, R.drawable.aot, ratingsDistribution = mapOf(
                1 to 10, 2 to 15, 3 to 30, 4 to 50, 5 to 100,
                6 to 300, 7 to 600, 8 to 900, 9 to 1200, 10 to 1500
            )),
            Anime(1, "Хуевое аниме", "хуйня", listOf("Боевик"), 9.0, 1999, 12, R.drawable.aot, ratingsDistribution = mapOf(
                1 to 10, 2 to 15, 3 to 30, 4 to 50, 5 to 100,
                6 to 300, 7 to 600, 8 to 900, 9 to 1200, 10 to 1500
            )),
            Anime(1, "Хуевое аниме", "хуйня", listOf("Боевик"), 9.0, 1999, 12, R.drawable.aot, ratingsDistribution = mapOf(
                1 to 10, 2 to 15, 3 to 30, 4 to 50, 5 to 100,
                6 to 300, 7 to 600, 8 to 900, 9 to 1200, 10 to 1500
            )),
            Anime(1, "Хуевое аниме", "хуйня", listOf("Боевик"), 9.0, 1999, 12, R.drawable.aot, ratingsDistribution = mapOf(
                1 to 10, 2 to 15, 3 to 30, 4 to 50, 5 to 100,
                6 to 300, 7 to 600, 8 to 900, 9 to 1200, 10 to 1500
            )),
            Anime(1, "Хуевое аниме", "хуйня", listOf("Боевик"), 9.0, 1999, 12, R.drawable.aot, ratingsDistribution = mapOf(
                1 to 10, 2 to 15, 3 to 30, 4 to 50, 5 to 100,
                6 to 300, 7 to 600, 8 to 900, 9 to 1200, 10 to 1500
            )),
            Anime(1, "Хуевое аниме", "хуйня", listOf("Боевик"), 9.0, 1999, 12, R.drawable.aot, ratingsDistribution = mapOf(
                1 to 10, 2 to 15, 3 to 30, 4 to 50, 5 to 100,
                6 to 300, 7 to 600, 8 to 900, 9 to 1200, 10 to 1500
            )),
            Anime(1, "Хуевое аниме", "хуйня", listOf("Боевик"), 9.0, 1999, 12, R.drawable.aot, ratingsDistribution = mapOf(
                1 to 10, 2 to 15, 3 to 30, 4 to 50, 5 to 100,
                6 to 300, 7 to 600, 8 to 900, 9 to 1200, 10 to 1500
            )),
            Anime(1, "Хуевое аниме", "хуйня", listOf("Боевик"), 9.0, 1999, 12, R.drawable.aot, ratingsDistribution = mapOf(
                1 to 10, 2 to 15, 3 to 30, 4 to 50, 5 to 100,
                6 to 300, 7 to 600, 8 to 900, 9 to 1200, 10 to 1500
            )),



            // Добавьте еще 8 элементов
        )

        binding.recommendationsRecyclerView.apply {
            // Передаем контекст (this@AnimeDetailsActivity), список и обработчик клика
            adapter = AnimeCardAdapter(
                context = this@AnimeDetailsActivity,
                animeList = recommendations,
                onItemClick = { anime ->
                    val intent = Intent(this@AnimeDetailsActivity, AnimeDetailsActivity::class.java).apply {
                        putExtra("anime", anime)
                    }
                    startActivity(intent)
                }
            )
            layoutManager = LinearLayoutManager(
                this@AnimeDetailsActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
        }
    }



    private fun getRandomColor(): Int {
        return listOf(
            Color.RED, Color.BLUE, Color.GREEN,
            Color.YELLOW, Color.MAGENTA, Color.CYAN
        ).random()
    }
}