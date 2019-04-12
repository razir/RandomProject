package com.anton.mercaritest.data.source

import com.anton.mercaritest.data.api.MercariApi
import com.anton.mercaritest.data.entity.Category
import io.reactivex.Single

interface CategoriesRepository {
    fun getCategories(): Single<List<Category>>
}

class CategoriesRepositoryImpl(private val mercariApi: MercariApi) : CategoriesRepository {

    override fun getCategories(): Single<List<Category>> {
        return mercariApi.getCategories()
    }
}