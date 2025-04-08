package co.feip.fefu2025

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var animeRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация RecyclerView
        animeRecyclerView = findViewById(R.id.animeRecyclerView)
        setupAnimeList()

        // Настройка Toolbar
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Настройка SearchView
        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setupAnimeList() {
        animeRecyclerView.layoutManager = GridLayoutManager(this, 2)
        animeRecyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                resources.getDimensionPixelSize(R.dimen.grid_spacing),
                true
            )
        )

        val animeList = listOf(
            Anime(
                id = 1,
                title = "Наруто",
                description = "История о юном ниндзя...",
                genres = listOf("Экшен", "Приключения", "Комедия"),
                rating = 8.5,
                year = 2002,
                episodes = 220,
                imageResId = R.drawable.naruto,
                ratingsDistribution = mapOf(
                    1 to 50, 2 to 30, 3 to 100, 4 to 200, 5 to 300,
                    6 to 500, 7 to 800, 8 to 1200, 9 to 900, 10 to 600
                )
            ),
            Anime(
                id = 2,
                title = "Маг целитель",
                description = "История про месть...",
                genres = listOf("Романтика", "Драма", "Комедия"),
                rating = 9.0,
                year = 2022,
                episodes = 12,
                imageResId = R.drawable.aot,
                ratingsDistribution = mapOf(
                    1 to 20, 2 to 15, 3 to 50, 4 to 100, 5 to 200,
                    6 to 300, 7 to 500, 8 to 800, 9 to 600, 10 to 400
                )
            ),
            Anime(
                id = 3,
                title = "Blue lock",
                description = "2018 год стал поворотным...",
                genres = listOf("Экшен", "Спорт", "Комедия"),
                rating = 8.7,
                year = 2022,
                episodes = 24,
                imageResId = R.drawable.bluelock,
                ratingsDistribution = mapOf(
                    1 to 10, 2 to 20, 3 to 40, 4 to 80, 5 to 160,
                    6 to 320, 7 to 640, 8 to 500, 9 to 300, 10 to 200
                )
            ),
            Anime(
                id = 4,
                title = "Я переродился торговым автоматом",
                description = "Самой распространенной причиной...",
                genres = listOf("Исэкай", "Фэнтези", "Комедия"),
                rating = 4.1,
                year = 2024,
                episodes = 12,
                imageResId = R.drawable.chto,
                ratingsDistribution = mapOf(
                    1 to 300, 2 to 200, 3 to 150, 4 to 100, 5 to 50,
                    6 to 30, 7 to 20, 8 to 10, 9 to 5, 10 to 2
                )
            )
        )

        val adapter = AnimeCardAdapter(this, animeList) { anime ->
            val intent = Intent(this, AnimeDetailsActivity::class.java)
            intent.putExtra("anime", anime)
            startActivity(intent)
        }

        animeRecyclerView.adapter = adapter
    }
}