package com.test.newsapp.data.network

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
    private val apiService: ApiService,
    private val query: String
) : RemoteMediator<Int, NewsEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    lastItem?.nextPageKey ?: 1
                }
            }

            val response = apiService.newsList(
                page = loadKey,
                pageSize = state.config.pageSize,
                query = query
            )

            newsDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newsDb.dao.clearAll()
                }
                if (response.isResponseSuccess()) {
                    val newsEntities = response.articles.map { it.toNewsEntity(loadKey + 1) }
                    newsDb.dao.upsertAll(newsEntities)
                } else {
                    MediatorResult.Error(Exception(response.message))
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