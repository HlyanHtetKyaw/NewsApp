package com.test.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.test.newsapp.ui.theme.NewsAppTheme
import com.test.newsapp.presentation.NewsDetailScreen
import com.test.newsapp.presentation.NewsListScreen
import com.test.newsapp.presentation.Screen
import com.test.newsapp.presentation.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val newsViewModel: NewsViewModel by viewModels()
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppTheme {
                navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }

    @Composable
    fun NavGraph(navController: NavHostController) {
        NavHost(
            navController = navController,
            startDestination = Screen.NewsListScreen.route
        ) {

            composable(Screen.NewsListScreen.route) {
                NewsListScreen(navController = navController, viewModel = newsViewModel)
            }

            composable(
                route = Screen.NewDetailScreen.route,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id")
                NewsDetailScreen(
                    navController = navController,
                    viewModel = newsViewModel,
                    newId = id
                )
            }
        }
    }
}
