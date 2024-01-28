package com.test.newsapp.data.repository

import com.test.newsapp.data.local.NewsDao
import com.test.newsapp.data.local.NewsEntity

class LocalRepository(private val newsDao: NewsDao) {

    suspend fun upsertAll(news: List<NewsEntity>) = newsDao.upsertAll(news)

    fun pagingSource(query: String) = newsDao.pagingSource(query)

    fun getNewsById(newsId: Int) = newsDao.getNewsById(newsId)

    suspend fun clearAll() = newsDao.clearAll()
}