package com.anton.mercaritest.data.api

import com.anton.mercaritest.data.entity.Category
import com.anton.mercaritest.data.entity.Product
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface TimelineApi {

    @GET("master.json")
    fun getCategories(): Single<List<Category>>

    @GET
    fun getCategoryProducts(@Url url: String): Single<List<Product>>
}