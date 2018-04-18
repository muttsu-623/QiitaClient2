package com.websarva.wings.android.qiitaclient2.client

import com.websarva.wings.android.qiitaclient2.model.Article
import rx.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleClient {

    @GET("/api/v2/items")
    fun search(@Query("query") query: String) : Observable<List<Article>>
}