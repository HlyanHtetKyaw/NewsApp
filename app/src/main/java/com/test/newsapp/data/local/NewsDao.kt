package com.test.newsapp.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Upsert
    suspend fun upsertAll(news: List<NewsEntity>)

    @Query("SELECT * FROM news_entity WHERE title LIKE '%' || :query || '%'")
    fun pagingSource(query: String = "bitcoin"): PagingSource<Int, NewsEntity>

    @Query("SELECT * FROM news_entity WHERE id = :newId")
    fun getNewsById(newId: Int): Flow<NewsEntity?>

    @Query("DELETE FROM news_entity")
    suspend fun clearAll()
}