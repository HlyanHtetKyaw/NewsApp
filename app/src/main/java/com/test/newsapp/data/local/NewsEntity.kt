package com.test.newsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="news_entity")
data class NewsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val author: String,
    val content: String,
    val publishedAt: String,
    val url: String,
    val urlToImage: String,
)