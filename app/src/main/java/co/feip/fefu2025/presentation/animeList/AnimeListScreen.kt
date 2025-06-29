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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import co.feip.fefu2025.presentation.components.GenreChip
import androidx.compose.ui.Alignment
import co.feip.fefu2025.presentation.search.SearchViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.TopAppBar


@Composable

fun AnimeListScreen(
    onAnimeClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: AnimeListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is UiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is UiState.Error -> {
            val message = (uiState as UiState.Error).message
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = message)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { viewModel.loadAnimeList() }) {
                        Text("Повторить")
                    }
                }
            }
        }
        is UiState.Success -> {
            val animeList = (uiState as UiState.Success<List<Anime>>).data
            Column(Modifier.fillMaxSize()) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Поиск") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onSearchClick() },
                    enabled = false, // запрещаем ввод, чтобы поле было "только для открытия"
                    readOnly = true,
                    trailingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Поиск")
                    }
                )
                LazyColumn {
                    items(animeList) { anime ->
                        AnimeCard(anime = anime, onClick = { onAnimeClick(anime.id) })
                    }
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onAnimeClick: (Int) -> Unit,
    viewModel: SearchViewModel = viewModel()
) {
    var query by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(query) {
        viewModel.search(query)
    }

    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Поиск аниме") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                }
            }
        )

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Введите название") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        when (uiState) {
            is UiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                val message = (uiState as UiState.Error).message
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(message)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { viewModel.search(query) }) {
                            Text("Повторить")
                        }
                    }
                }
            }
            is UiState.Success<*> -> {
                val results = (uiState as UiState.Success<List<Anime>>).data
                if (results.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Результаты отсутствуют")
                    }
                } else {
                    LazyColumn {
                        items(results) { anime ->
                            AnimeCard(anime = anime, onClick = { onAnimeClick(anime.id) })
                        }
                    }
                }
            }
        }
    }
}

