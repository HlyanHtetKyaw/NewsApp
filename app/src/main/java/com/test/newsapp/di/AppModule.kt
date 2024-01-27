package com.test.newsapp.di

import android.content.Context
import android.content.res.Resources
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.test.newsapp.BuildConfig
import com.test.newsapp.data.local.NewsDao
import com.test.newsapp.data.local.NewsDatabase
import com.test.newsapp.data.local.NewsEntity
import com.test.newsapp.data.network.ApiService
import com.test.newsapp.data.network.NewsRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }


    @Singleton
    @Provides
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideGsonConverter() = GsonBuilder()
        .setLenient()
        .create()!!


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gSon: Gson): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gSon))
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources = context.resources

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            "news.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNewsPager(newsDb: NewsDatabase, apiService: ApiService): Pager<Int, NewsEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = NewsRemoteMediator(
                newsDb = newsDb,
                apiService = apiService
            ),
            pagingSourceFactory = {
                newsDb.dao.pagingSource()
            }
        )
    }

    @Singleton
    @Provides
    fun provideNewsDao(newsDb: NewsDatabase): NewsDao {
        return newsDb.dao
    }


}