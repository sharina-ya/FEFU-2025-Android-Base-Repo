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
import co.feip.fefu2025.domain.model.Anime
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.verticalScroll
import co.feip.fefu2025.presentation.animeList.AnimeCard

import co.feip.fefu2025.presentation.components.GenreChip

import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import co.feip.fefu2025.presentation.animeList.AnimeListViewModel
import co.feip.fefu2025.presentation.animeList.UiState

import co.feip.fefu2025.presentation.animeList.RecommendationsViewModel

@Composable
fun AnimeDetailsScreen(
    animeId: Int,
    onAnimeClick: (Int) -> Unit,
    onBack: () -> Unit,
    onRecommendationsClick: (Int) -> Unit,
    viewModel: AnimeDetailsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(animeId) {
        viewModel.loadAnimeById(animeId)
    }

    when (uiState) {
        is UiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is UiState.Error -> {
            val message = (uiState as UiState.Error).message
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ошибка: $message", color = Color.Red)
            }
        }
        is UiState.Success -> {
            val anime = (uiState as UiState.Success<Anime?>).data
            if (anime == null) {
                Text("Аниме не найдено")
            } else {
                AnimeDetailsContent(anime, onAnimeClick, onBack, onRecommendationsClick)
            }
        }
        else -> {}
    }
}




@Composable
fun AnimeDetailsContent(
    anime: Anime,
    onAnimeClick: (Int) -> Unit,
    onBack: () -> Unit,
    onRecommendationsClick: (Int) -> Unit
) {
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
            if (anime.imageResId != 0) {
                Image(
                    painter = painterResource(id = anime.imageResId),
                    contentDescription = anime.title,
                    modifier = Modifier.size(140.dp).padding(end = 16.dp)
                )
            } else if (anime.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = anime.imageUrl,
                    contentDescription = anime.title,
                    modifier = Modifier.size(140.dp).padding(end = 16.dp)
                )
            } else {
                // Можно отобразить заглушку или ничего
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .padding(end = 16.dp)
                        .background(Color.Gray)
                )
            }

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
        Text(
            "Может понравиться:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .clickable { onRecommendationsClick(anime.id) }
                .padding(vertical = 8.dp)
        )

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

// Этот код остается таким же
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsScreen(
    onAnimeClick: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: RecommendationsViewModel = viewModel() // Используем новый ViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // LaunchedEffect(Unit) не нужен, так как init{} в ViewModel уже вызывает загрузку

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Все аниме") }, // Можно изменить заголовок
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        when (uiState) {
            is UiState.Loading -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            is UiState.Error -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Ошибка: ${(uiState as UiState.Error).message}", color = Color.Red)
            }
            is UiState.Success -> {
                val allAnime = (uiState as UiState.Success<List<Anime>>).data
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(allAnime) { anime ->
                        AnimeCard(anime = anime, onClick = { onAnimeClick(anime.id) })
                    }
                }
            }
            else -> {}
        }
    }
}
