package co.feip.fefu2025.presentation.animeList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.feip.fefu2025.domain.model.Anime
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
@Composable
fun AnimeListScreen(
    onAnimeClick: (Int) -> Unit,
    viewModel: AnimeListViewModel = viewModel()
) {
    val animeList by viewModel.animeList.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Поиск") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        LazyColumn {
            items(animeList.filter {
                it.title.contains(searchQuery, true) ||
                        it.genres.any { g -> g.contains(searchQuery, true) } ||
                        it.description.contains(searchQuery, true)
            }) { anime ->
                AnimeCard(anime = anime, onClick = { onAnimeClick(anime.id) })
            }
        }
    }
}

@Composable
fun AnimeCard(anime: Anime, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = anime.imageResId),
                contentDescription = anime.title,
                modifier = Modifier
                    .size(90.dp)
                    .padding(end = 12.dp)
            )
            Column {
                Text(anime.title, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Row {
                    anime.genres.forEach { genre ->
                        GenreChip(genre)
                        Spacer(Modifier.width(4.dp))
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text("Рейтинг: ${anime.rating}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun GenreChip(genre: String) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.padding(end = 2.dp)
    ) {
        Text(
            text = genre,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
