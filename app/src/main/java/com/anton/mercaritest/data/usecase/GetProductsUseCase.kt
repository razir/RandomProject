package com.anton.mercaritest.data.usecase

import com.anton.mercaritest.data.entity.Category
import com.anton.mercaritest.data.entity.Product
import com.anton.mercaritest.data.source.ProductsRepository
import io.reactivex.Single

interface GetProductsUseCase {

    fun execute(category: Category, forceRefresh: Boolean): Single<List<Product>>
}

class GetProductsUseCaseImpl(private val productsRepository: ProductsRepository) : GetProductsUseCase {

    override fun execute(category: Category, forceRefresh: Boolean): Single<List<Product>> {
        return category.data?.let { url ->
            if (!forceRefresh) {
                Single.fromCallable {
                    productsRepository.getCachedProducts(url.hashCode().toString())
                }.flatMap {
                    if (it.isEmpty()) {
                        loadProducts(url)
                    } else {
                        Single.just(it)
                    }
                }
            } else {
                return loadProducts(url)
            }
        } ?: kotlin.run {
            Single.error<List<Product>>(Throwable("Category url is empty"))
        }
    }

    private fun loadProducts(url: String): Single<List<Product>> {
        return productsRepository.getProducts(url)
            .doOnSuccess {
                productsRepository.saveProductsCache(it, url.hashCode().toString())
            }
    }

}