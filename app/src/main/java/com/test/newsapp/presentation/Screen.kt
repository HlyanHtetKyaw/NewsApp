package com.test.newsapp.presentation

sealed class Screen(val route: String) {
    data object NewsListScreen : Screen("list")

    data object NewDetailScreen : Screen("details/{id}")

}