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
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import co.feip.fefu2025.presentation.components.GenreChip
import androidx.compose.ui.Alignment
import co.feip.fefu2025.presentation.search.SearchViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeListScreen(
    onAnimeClick: (Int) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: AnimeListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val popularUiState by viewModel.popularUiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Поле поиска
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSearchClick() }
                .padding(16.dp),
            enabled = false,
            placeholder = { Text("Поиск аниме") }
        )

        Text("Популярные аниме", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(start = 16.dp))

        when (popularUiState) {
            is UiState.Loading -> {
                Box(Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                val message = (popularUiState as UiState.Error).message
                Text("Ошибка: $message", color = Color.Red, modifier = Modifier.padding(16.dp))
            }
            is UiState.Success -> {
                val popularList = (popularUiState as UiState.Success<List<Anime>>).data
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(vertical = 8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    itemsIndexed(popularList) { index, anime ->
                        PopularAnimeCard(anime = anime, onClick = { onAnimeClick(anime.id) })

                        if (index == popularList.lastIndex - 1) {
                            LaunchedEffect(Unit) {
                                viewModel.loadNextPopularPage()
                            }
                        }
                    }
                }
            }

            else -> {}
        }

        Spacer(Modifier.height(16.dp))

        // Основной список аниме
        when (uiState) {
            is UiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                val message = (uiState as UiState.Error).message
                Text("Ошибка: $message", color = Color.Red, modifier = Modifier.padding(16.dp))
            }
            is UiState.Success -> {
                val animeList = (uiState as UiState.Success<List<Anime>>).data
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(animeList) { anime ->
                        AnimeCard(anime = anime, onClick = { onAnimeClick(anime.id) })
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
fun PopularAnimeCard(anime: Anime, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(width = 140.dp, height = 200.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(8.dp)) {
            AsyncImage(
                model = anime.imageUrl, // теперь поле есть
                contentDescription = anime.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(120.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(anime.title, maxLines = 2, style = MaterialTheme.typography.bodyMedium)
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
}@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onAnimeClick: (Int) -> Unit,
    viewModel: SearchViewModel = viewModel()
) {
    var query by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    // Debounce для запроса поиска: ждём 500 мс после последнего ввода, чтобы не гонять слишком много запросов
    LaunchedEffect(query) {
        snapshotFlow { query }
            .debounce(500)
            .distinctUntilChanged()
            .collectLatest { searchText ->
                if (searchText.isNotBlank()) {
                    viewModel.searchAnime(searchText) // Запускаем поиск по популярным аниме
                } else {
                    viewModel.reset() // Сбрасываем результаты, если строка пустая
                }
            }
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
                .padding(8.dp),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )

        when (uiState) {
            is UiState.Loading -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            is UiState.Error -> {
                val message = (uiState as UiState.Error).message
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(message, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { viewModel.searchAnime(query) }) {
                            Text("Повторить")
                        }
                    }
                }
            }
            is UiState.Success -> {
                val results = (uiState as UiState.Success<List<Anime>>).data
                if (results.isEmpty()) {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Результаты отсутствуют")
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(results) { anime ->
                            AnimeCard(anime = anime, onClick = { onAnimeClick(anime.id) })
                        }
                        // Индикатор загрузки внизу списка при подгрузке следующей страницы
                        item {
                            if (viewModel.isLoading) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }

                    // Пагинация: подгружаем следующую страницу, когда пользователь близок к концу списка
                    val shouldLoadMore = remember {
                        derivedStateOf {
                            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                            lastVisibleItem >= results.size - 5 && !viewModel.isLoading
                        }
                    }
                    LaunchedEffect(shouldLoadMore.value) {
                        if (shouldLoadMore.value) {
                            viewModel.loadNextPage()
                        }
                    }
                }
            }
            else -> {}
        }
    }
}
