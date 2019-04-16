package com.anton.mercaritest.data.usecase

import com.anton.mercaritest.data.source.ProductsRepository
import io.reactivex.Completable

interface CleanProductsCacheUseCase {

    fun execute(): Completable
}

class CleanProductsCacheUseCaseImpl(private val productsRepository: ProductsRepository) : CleanProductsCacheUseCase {

    override fun execute(): Completable {
        return Completable.fromCallable {
            productsRepository.clearCache()
        }
    }
}