package com.example.animegenres

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private val genres = listOf(
        Pair("Романтика", Color.RED),
        Pair("Экшен", Color.BLUE),
        Pair("Комедия", Color.GREEN),
        Pair("Фантастика", Color.YELLOW),
        Pair("Ужасы", Color.MAGENTA),
        Pair("Приключения", Color.CYAN),
        Pair("Драма", Color.LTGRAY)
    )

    private lateinit var flexBoxLayout: MyFlexBoxLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flexBoxLayout = findViewById(R.id.flexBoxLayout)
        val addButton: Button = findViewById(R.id.addButton)

        addButton.setOnClickListener {
            addGenre()
        }
    }

    private fun addGenre() {
        val randomIndex = Random().nextInt(genres.size)
        val genre = genres[randomIndex]

        val genreView = AnimeGenreView(this)
        genreView.setGenre(genre.first, genre.second)

        flexBoxLayout.addView(genreView)
    }
}