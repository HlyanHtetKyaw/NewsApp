package com.test.newsapp.presentation

sealed class Screen(val route: String) {
    object NewsListScreen : Screen("list")

    object NewDetailScreen : Screen("details/{id}")

}