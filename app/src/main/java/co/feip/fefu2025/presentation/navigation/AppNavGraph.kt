package co.feip.fefu2025.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import co.feip.fefu2025.presentation.animeDetails.AnimeDetailsScreen
import co.feip.fefu2025.presentation.animeList.AnimeListScreen
import co.feip.fefu2025.presentation.animeList.SearchScreen

object Destinations {
    const val MAIN = "main"
    const val DETAILS = "details/{animeId}"
    const val SEARCH = "search"
    fun details(animeId: Int) = "details/$animeId"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "animeList") {
        composable("animeList") {
            AnimeListScreen(
                onAnimeClick = { animeId ->
                    navController.navigate("animeDetails/$animeId")
                },
                onSearchClick = {
                    navController.navigate("search")
                }
            )
        }
        composable(
            "animeDetails/{animeId}",
            arguments = listOf(navArgument("animeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt("animeId") ?: 0
            AnimeDetailsScreen(
                animeId = animeId,
                onAnimeClick = { newId ->
                    // Навигация на новый экран деталей с другим animeId
                    navController.navigate("animeDetails/$newId")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("search") {
            SearchScreen(
                onBack = { navController.popBackStack() },
                onAnimeClick = { animeId ->
                    navController.navigate("animeDetails/$animeId")
                }
            )
        }
    }
}

