package com.anton.mercaritest.data.source

import com.anton.mercaritest.data.api.TimelineApi
import com.anton.mercaritest.data.entity.Category
import io.reactivex.Single

interface CategoriesRepository {
    fun getCategories(): Single<List<Category>>
}

class CategoriesRepositoryImpl(private val timelineApi: TimelineApi) : CategoriesRepository {

    override fun getCategories(): Single<List<Category>> {
        return timelineApi.getCategories()
    }
}