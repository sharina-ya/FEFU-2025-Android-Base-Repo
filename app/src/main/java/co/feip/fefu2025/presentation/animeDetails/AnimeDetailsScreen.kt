package co.feip.fefu2025.presentation.animeDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.feip.fefu2025.data.repository.MockAnimeRepository
import co.feip.fefu2025.domain.model.Anime
import androidx.compose.foundation.Image
import androidx.compose.foundation.verticalScroll

import co.feip.fefu2025.presentation.animeList.AnimeListScreen
import co.feip.fefu2025.presentation.components.GenreChip

@Composable
fun AnimeDetailsScreen(
    animeId: Int,
    onAnimeClick: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: AnimeDetailsViewModel = viewModel()
) {
    val anime = remember(animeId) { MockAnimeRepository.getAnimeById(animeId) }
    if (anime == null) {
        Text("Аниме не найдено")
        return
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = anime.imageResId),
                contentDescription = anime.title,
                modifier = Modifier
                    .size(140.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(anime.title, style = MaterialTheme.typography.headlineSmall)
                Text("Год: ${anime.year}, Серий: ${anime.episodes}")
                Row(Modifier.horizontalScroll(rememberScrollState())) {
                    anime.genres.forEach { genre ->
                        GenreChip(genre)
                        Spacer(Modifier.width(4.dp))
                    }
                }
                Text("Рейтинг: ${anime.rating}")
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(anime.description)
        Spacer(Modifier.height(16.dp))
        RatingChart(anime)
        Spacer(Modifier.height(24.dp))
        Text("Может понравиться:", style = MaterialTheme.typography.titleMedium)
        AnimeRecommendationsSection(currentAnimeId = anime.id, onAnimeClick = onAnimeClick)
    }
}

@Composable
fun RatingChart(anime: Anime) {
    val ratings = anime.ratingsDistribution.ifEmpty {
        mapOf(
            1 to 100, 2 to 50, 3 to 200, 4 to 300, 5 to 450,
            6 to 600, 7 to 800, 8 to 700, 9 to 400, 10 to 500
        )
    }
    val maxCount = ratings.values.maxOrNull() ?: 1
    Row(Modifier.height(100.dp).fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
        for (i in 1..10) {
            val count = ratings[i] ?: 0
            val barHeight = (count.toFloat() / maxCount * 80).dp
            Box(
                Modifier
                    .weight(1f)
                    .height(barHeight)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(Modifier.width(2.dp))
        }
    }
    Row(Modifier.fillMaxWidth()) {
        for (i in 1..10) {
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text("$i", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun AnimeRecommendationsSection(
    currentAnimeId: Int,
    onAnimeClick: (Int) -> Unit
) {
    val animeList = remember { MockAnimeRepository.getAnimeList() }
    val recommendations = remember(animeList, currentAnimeId) {
        animeList.filter { it.id != currentAnimeId }.shuffled().take(10)
    }
    LazyRow {
        items(recommendations) { anime ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .width(180.dp)
                    .height(220.dp)
                    .clickable { onAnimeClick(anime.id) },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = anime.imageResId),
                        contentDescription = anime.title,
                        modifier = Modifier.size(90.dp)
                    )
                    Text(anime.title, style = MaterialTheme.typography.titleSmall)
                    Text("Рейтинг: ${anime.rating}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
