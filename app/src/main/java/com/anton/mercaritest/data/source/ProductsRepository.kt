package com.anton.mercaritest.data.source

import com.anton.mercaritest.data.api.TimelineApi
import com.anton.mercaritest.data.entity.Product
import io.reactivex.Single

interface ProductsRepository {
    fun getProducts(url: String): Single<List<Product>>

    fun getCachedProducts(categoryHash: String): List<Product>

    fun saveProductsCache(products: List<Product>, categoryHash: String)

    fun clearCache()
}

class ProductsRepositoryImpl(private val timelineApi: TimelineApi, private val productsDao: ProductsDao) :
    ProductsRepository {

    override fun getProducts(url: String): Single<List<Product>> {
        return timelineApi.getCategoryProducts(url)
    }

    override fun getCachedProducts(categoryHash: String): List<Product> {
        return productsDao.getAllByCategory(categoryHash)
    }

    override fun saveProductsCache(products: List<Product>, categoryHash: String) {
        productsDao.deleteWithCategory(categoryHash)
        products.forEach {
            it.categoryHash = categoryHash
        }
        productsDao.save(products)
    }

    override fun clearCache() {
        productsDao.deleteAll()
    }
}