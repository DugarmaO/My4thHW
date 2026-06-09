package com.example.my4thhw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.my4thhw.ui.theme.My4thHWTheme
import com.example.my4thhw.uii.MangaViewModel
import com.example.my4thhw.uii.screens.MangaDetailsScreen
import com.example.my4thhw.uii.screens.MangaListScreen
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            My4thHWTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MangaApp()
                }
            }
        }
    }
}

object Routes {
    const val LIST = "list"
    const val DETAILS = "details/{id}"
    fun details(id: Int) = "details/$id"
}

@androidx.compose.runtime.Composable
fun MangaApp() {
    val vm: MangaViewModel = hiltViewModel()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LIST
    ) {
        composable(Routes.LIST) {
            val ui by vm.uiState.collectAsState()

            MangaListScreen(
                ui = ui,
                onSearchChange = vm::onSearchChange,
                onItemClick = { id -> navController.navigate(Routes.details(id)) },
                onFavoriteClick = vm::onFavouriteClick,
                onRetry = {
                    val query = vm.uiState.value.query
                    if (query.isBlank()) vm.loadInitial()
                    else vm.onSearchChange(query)
                },
                onToggleFavourites = vm::toggleShowFavourites,
            )
        }

        composable(
            route = Routes.DETAILS,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            val detailsState by vm.detailsState.collectAsState()

            LaunchedEffect(id) {
                vm.loadDetails(id)
            }

            MangaDetailsScreen(
                ui = detailsState,
                onBack = { navController.navigateUp() },
                onRetry = { vm.loadDetails(id) },
                onFavoriteToggle = vm::onFavouriteToggle,
            )
        }
    }
}