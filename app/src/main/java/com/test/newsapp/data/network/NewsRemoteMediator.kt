package com.test.newsapp.data.network

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.test.newsapp.data.local.NewsDatabase
import com.test.newsapp.data.local.NewsEntity
import com.test.newsapp.data.mappers.toNewsEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val newsDb: NewsDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, NewsEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>
    ): MediatorResult {
        return try {
            Log.d("NewsRemoteMediator", "load: "+loadType)
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        1
                    } else {
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }

            val response = apiService.newsList(
                page = loadKey,
                pageSize = state.config.pageSize
            )

            newsDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newsDb.dao.clearAll()
                }
                if (response.isResponseSuccess()) {
                    val newsEntities = response.articles.map { it.toNewsEntity() }
                    newsDb.dao.upsertAll(newsEntities)
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = response.articles.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}