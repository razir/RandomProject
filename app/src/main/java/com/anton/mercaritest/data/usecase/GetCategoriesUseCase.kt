package com.anton.mercaritest.data.usecase

import com.anton.mercaritest.data.entity.Category
import com.anton.mercaritest.data.source.CategoriesRepository
import io.reactivex.Single

interface GetCategoriesUseCase {
    fun execute(): Single<List<Category>>
}

class GetCategoriesUseCaseImpl(private val categoriesRepository: CategoriesRepository) : GetCategoriesUseCase {

    override fun execute(): Single<List<Category>> {
        return categoriesRepository.getCategories()
    }

}