package co.feip.fefu2025.presentation.animeDetails

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import co.feip.fefu2025.domain.model.Anime
import co.feip.fefu2025.R
import co.feip.fefu2025.databinding.ActivityAnimeDetailsBinding
import co.feip.fefu2025.presentation.components.AnimeCardAdapter
import co.feip.fefu2025.presentation.components.AnimeGenreView

class AnimeDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimeDetailsBinding
    private val viewModel: AnimeDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем аниме из интента и передаём в ViewModel
        val animeFromIntent = intent.getParcelableExtra<Anime>("anime")
        if (animeFromIntent == null) {
            finish()
            return
        }
        viewModel.loadAnimeDetails(animeFromIntent)

        // Подписываемся на данные из ViewModel
        viewModel.anime.observe(this, Observer { anime ->
            setupUI(anime)
            setupRatingChart(anime)
            setupRecommendations()
        })

        binding.returnBtn.setOnClickListener { finish() }
    }

    private fun setupUI(anime: Anime) {
        with(binding) {
            animeImage.setImageResource(anime.imageResId)
            animeTitle.text = anime.title
            animeRating.text = anime.rating.toString()
            animeYear.text = anime.year.toString()
            animeEpisodes.text = "${anime.episodes} эп."
            animeDescription.text = anime.description

            genresContainer.removeAllViews()
            anime.genres.forEach { genre ->
                AnimeGenreView(this@AnimeDetailsActivity).apply {
                    setGenre(genre, getRandomColor())
                    genresContainer.addView(this)
                }
            }
        }
    }

    private fun setupRatingChart(anime: Anime) {
        val ratings = anime.ratingsDistribution.ifEmpty {
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
            View(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    (count.toFloat() / maxCount * 150).toInt(),
                    1f
                ).apply { setMargins(2) }
                setBackgroundColor(ContextCompat.getColor(this@AnimeDetailsActivity, R.color.blue))
                chartContainer.addView(this)
            }

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

            )

        binding.recommendationsRecyclerView.apply {
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
            layoutManager = LinearLayoutManager(this@AnimeDetailsActivity, LinearLayoutManager.HORIZONTAL, false)
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
