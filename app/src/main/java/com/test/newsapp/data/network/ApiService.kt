package com.test.newsapp.data.network

import com.test.newsapp.BuildConfig
import com.test.newsapp.data.network.EndPoints.NEWS_LIST
import com.test.newsapp.data.network.response.ArticleResponse
import com.test.newsapp.data.network.response.BaseObjectListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(NEWS_LIST)
    suspend fun newsList(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
    ): BaseObjectListResponse<ArticleResponse>

}