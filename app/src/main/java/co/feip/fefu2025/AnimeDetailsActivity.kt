package co.feip.fefu2025

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AnimeDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_details)

        findViewById<Button>(R.id.return_btn).setOnClickListener {
            finish()
        }

        val anime = intent.getParcelableExtra<Anime>("anime") ?: return
        val animeImage: ImageView = findViewById(R.id.animeImage)
        val animeTitle: TextView = findViewById(R.id.animeTitle)
        val genresContainer: MyFlexBoxLayout = findViewById(R.id.genresContainer)
        val animeRating: TextView = findViewById(R.id.animeRating)
        val animeYear: TextView = findViewById(R.id.animeYear)
        val animeEpisodes: TextView = findViewById(R.id.animeEpisodes)
        val animeDescription: TextView = findViewById(R.id.animeDescription)

        animeImage.setImageResource(anime.imageResId)
        animeTitle.text = anime.title
        animeRating.text = anime.rating.toString()
        animeYear.text = anime.year.toString()
        animeEpisodes.text = "${anime.episodes} эп."
        animeDescription.text = anime.description

        genresContainer.removeAllViews()

        anime.genres.forEach { genre ->
            val genreView = AnimeGenreView(this)
            genreView.setGenre(genre, getRandomColor())
            genresContainer.addView(genreView)
        }
    }

    private fun getRandomColor(): Int {
        val colors = listOf(
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.MAGENTA,
            Color.CYAN,
            Color.LTGRAY
        )
        return colors.random()
    }
}