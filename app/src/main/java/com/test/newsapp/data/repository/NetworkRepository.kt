package com.test.newsapp.data.repository

import com.test.newsapp.data.network.ApiService

class NetworkRepository(private val apiService: ApiService) {

    suspend fun getNewsList(page: Int, pageSize: Int, query: String) =
        apiService.newsList(
            page = page,
            pageSize = pageSize,
            query = query
        )

}