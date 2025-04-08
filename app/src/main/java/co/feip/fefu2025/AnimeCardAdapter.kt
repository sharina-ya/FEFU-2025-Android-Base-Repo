package co.feip.fefu2025


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.feip.fefu2025.ui.components.RatingBarChart

class AnimeCardAdapter(
    private val context: Context,
    private val animeList: List<Anime>,
    private val onItemClick: (Anime) -> Unit
) : RecyclerView.Adapter<AnimeCardAdapter.AnimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_anime_card, parent, false)
        return AnimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        val anime = animeList[position]
        holder.bind(anime)
        holder.itemView.setOnClickListener { onItemClick(anime) }
    }

    override fun getItemCount(): Int = animeList.size

    class AnimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val animeImage: ImageView = itemView.findViewById(R.id.animeImage)
        private val animeTitle: TextView = itemView.findViewById(R.id.animeTitle)
        private val genresContainer: MyFlexBoxLayout = itemView.findViewById(R.id.genresContainer)
        private val animeRating: TextView = itemView.findViewById(R.id.animeRating)

        fun bind(anime: Anime) {
            animeImage.setImageResource(anime.imageResId)
            animeTitle.text = anime.title
            animeRating.text = anime.rating.toString()
            genresContainer.removeAllViews()

            anime.genres.forEach { genre ->
                val genreView = AnimeGenreView(itemView.context)
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
}