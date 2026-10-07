package com.pemmob.gamecatalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pemmob.gamecatalog.ui.screen.AboutScreen
import com.pemmob.gamecatalog.ui.screen.GameDetailScreen
import com.pemmob.gamecatalog.ui.screen.HomeScreen
import com.pemmob.gamecatalog.ui.theme.GameCatalogTheme
import com.pemmob.gamecatalog.ui.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameCatalogTheme {
                val navController = rememberNavController()
                val gameViewModel: GameViewModel = viewModel()

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            viewModel = gameViewModel,
                            onGameClick = { id -> navController.navigate("detail/$id") },
                            onAboutClick = { navController.navigate("about") }
                        )
                    }
                    composable(
                        route = "detail/{gameId}",
                        arguments = listOf(navArgument("gameId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val gameId = backStackEntry.arguments?.getInt("gameId") ?: 0
                        GameDetailScreen(
                            gameId = gameId,
                            viewModel = gameViewModel,
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                    composable("about") {
                        AboutScreen(onBackClick = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}