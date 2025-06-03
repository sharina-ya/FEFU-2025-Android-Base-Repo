package co.feip.fefu2025.presentation.animeList

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.feip.fefu2025.presentation.components.AnimeCardAdapter
import co.feip.fefu2025.presentation.animeDetails.AnimeDetailsActivity
import co.feip.fefu2025.presentation.components.GridSpacingItemDecoration
import co.feip.fefu2025.R
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var animeRecyclerView: RecyclerView
    private lateinit var animeAdapter: AnimeCardAdapter
    private val animeListViewModel: AnimeListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        animeRecyclerView = findViewById(R.id.animeRecyclerView)
        animeRecyclerView.layoutManager = GridLayoutManager(this, 2)
        animeRecyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                resources.getDimensionPixelSize(R.dimen.grid_spacing),
                true
            )
        )

        animeAdapter = AnimeCardAdapter(this, emptyList()) { anime ->
            val intent = Intent(this, AnimeDetailsActivity::class.java)
            intent.putExtra("anime", anime)
            startActivity(intent)
        }
        animeRecyclerView.adapter = animeAdapter

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterAnimeList(newText)
                return true
            }
        })

        animeListViewModel.animeList.observe(this, Observer { list ->
            animeAdapter.updateData(list)
        })
    }

    private fun filterAnimeList(query: String?) {
        val currentList = animeListViewModel.animeList.value ?: return
        val filteredList = if (query.isNullOrBlank()) {
            currentList
        } else {
            currentList.filter { anime ->
                anime.title.contains(query, ignoreCase = true) ||
                        anime.genres.any { it.contains(query, ignoreCase = true) } ||
                        anime.description.contains(query, ignoreCase = true)
            }
        }
        animeAdapter.updateData(filteredList)
    }
}
