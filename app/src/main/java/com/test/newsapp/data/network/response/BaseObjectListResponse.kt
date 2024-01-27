package com.test.newsapp.data.network.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
open class BaseObjectListResponse<T> {

    @SerializedName("status")
    val status: String = ""

    @SerializedName("totalResults")
    val totalResults: Int = 0

    @SerializedName("articles")
    val articles: MutableList<T> = ArrayList()

    fun isResponseSuccess(): Boolean {
        return status == "ok"
    }
}