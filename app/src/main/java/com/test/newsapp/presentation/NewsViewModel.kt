package com.test.newsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.test.newsapp.data.local.NewsDatabase
import com.test.newsapp.data.local.NewsEntity
import com.test.newsapp.data.mappers.toNews
import com.test.newsapp.data.network.ApiService
import com.test.newsapp.data.network.NewsRemoteMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsDb: NewsDatabase,
    apiService: ApiService
) : ViewModel() {

    val queryFlow = MutableStateFlow("bitcoin")

    val newsPagingFlow = queryFlow.flatMapLatest { query ->
        Pager(config = PagingConfig(pageSize = 10),
            remoteMediator = NewsRemoteMediator(
                newsDb = newsDb,
                apiService = apiService,
                query = query
            ),
            pagingSourceFactory = {
                newsDb.dao.pagingSource(query)
            }
        ).flow
            .map { pagingData ->
                pagingData.map { it.toNews() }
            }
            .cachedIn(viewModelScope)
    }


    fun getNewsById(id: Int): Flow<NewsEntity?> {
        return newsDb.dao.getNewsById(id)
    }

    fun searchNews(query: String) {
        queryFlow.value = query
    }
}