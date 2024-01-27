package com.test.newsapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.test.newsapp.data.local.NewsDao
import com.test.newsapp.data.local.NewsEntity
import com.test.newsapp.data.mappers.toNews
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val pager: Pager<Int, NewsEntity>,
    private val newsDao: NewsDao
) : ViewModel() {

    val newsPagingFlow = pager
        .flow
        .map { pagingData ->
            pagingData.map { it.toNews() }
        }
        .cachedIn(viewModelScope)

    fun getNewsById(id: Int): Flow<NewsEntity?> {
        return newsDao.getNewsById(id)
    }

}