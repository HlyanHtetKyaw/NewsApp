package com.test.newsapp.data.mappers

import com.test.newsapp.data.local.NewsEntity
import com.test.newsapp.data.network.response.ArticleResponse
import com.test.newsapp.domain.News

fun ArticleResponse.toNewsEntity(nextPageKey: Int): NewsEntity {
    return NewsEntity(
        title = title ?: "Empty Title",
        description = description ?: "Empty Description",
        author = author ?: "Empty Author",
        content = content ?: "Empty Content",
        publishedAt = publishedAt ?: "Empty Published At",
        url = url ?: "Empty url",
        urlToImage = urlToImage ?: "Empty urlToImage",
        nextPageKey = nextPageKey
    )
}

fun NewsEntity.toNews(): News {
    return News(
        id = id,
        title = title,
        description = description,
        author = author,
        content = content,
        publishedAt = publishedAt,
        url = url,
        urlToImage = urlToImage
    )
}