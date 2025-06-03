package co.feip.fefu2025.presentation.components

import android.content.Context

import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.FrameLayout
import co.feip.fefu2025.R

class AnimeGenreView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val genreTextView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_anime_genre, this, true)
        genreTextView = findViewById(R.id.genreTextView)
    }

    fun setGenre(name: String, color: Int) {
        genreTextView.text = name
        genreTextView.background.setTint(color)
    }
}