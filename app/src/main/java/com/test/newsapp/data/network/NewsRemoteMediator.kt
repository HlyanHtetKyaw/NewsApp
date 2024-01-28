package com.test.newsapp.data.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.test.newsapp.data.local.NewsDatabase
import com.test.newsapp.data.local.NewsEntity
import com.test.newsapp.data.mappers.toNewsEntity
import com.test.newsapp.data.repository.LocalRepository
import com.test.newsapp.data.repository.NetworkRepository
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val newsDb: NewsDatabase,
    private val networkRepository: NetworkRepository,
    private val localRepository: LocalRepository,
    private val query: String
) : RemoteMediator<Int, NewsEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>
    ): MediatorResult {
        return try {
            delay(1000)

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
            val response = networkRepository.getNewsList(
                page = loadKey,
                pageSize = state.config.pageSize,
                query = query
            )

            newsDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    localRepository.clearAll()
                }
                if (response.isResponseSuccess()) {
                    val newsEntities = response.articles.map { it.toNewsEntity(loadKey + 1) }
                    localRepository.upsertAll(newsEntities)
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