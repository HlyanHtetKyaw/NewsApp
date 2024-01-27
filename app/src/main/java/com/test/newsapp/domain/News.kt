package com.test.newsapp.domain

data class News(
    val id: Int,
    val title: String,
    val description: String,
    val author: String,
    val content: String,
    val publishedAt: String,
    val url: String,
    val urlToImage: String,
)