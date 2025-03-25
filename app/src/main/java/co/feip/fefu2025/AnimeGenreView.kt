package com.example.animegenres

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.FrameLayout
import com.example.animegenres.R

class AnimeGenreView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var genreTextView: TextView

    init {
// Инфлейтим XML-ресурс
        LayoutInflater.from(context).inflate(R.layout.view_anime_genre, this, true)
        genreTextView = findViewById(R.id.genreTextView)
    }

    fun setGenre(name: String, color: Int) {
        genreTextView.text = name
        setBackgroundColor(color)
    }
}