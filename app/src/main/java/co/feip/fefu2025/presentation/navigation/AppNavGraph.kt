package co.feip.fefu2025.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import co.feip.fefu2025.presentation.animeList.AnimeListScreen
import co.feip.fefu2025.presentation.animeDetails.AnimeDetailsScreen

object Destinations {
    const val MAIN = "main"
    const val DETAILS = "details/{animeId}"
    fun details(animeId: Int) = "details/$animeId"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Destinations.MAIN
    ) {
        composable(Destinations.MAIN) {
            AnimeListScreen(
                onAnimeClick = { animeId ->
                    navController.navigate(Destinations.details(animeId))
                }
            )
        }
        composable(
            route = Destinations.DETAILS,
            arguments = listOf(navArgument("animeId") { type = NavType.IntType }),
            deepLinks = listOf(navDeepLink { uriPattern = "mysuperapp://anime/{animeId}" })
        ) { backStackEntry ->
            val animeId = backStackEntry.arguments?.getInt("animeId") ?: return@composable
            AnimeDetailsScreen(
                animeId = animeId,
                onAnimeClick = { id ->
                    navController.navigate(Destinations.details(id))
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
