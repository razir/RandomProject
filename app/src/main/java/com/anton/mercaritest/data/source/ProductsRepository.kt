package com.anton.mercaritest.data.source

import com.anton.mercaritest.data.api.MercariApi
import com.anton.mercaritest.data.entity.Product
import io.reactivex.Single

interface ProductsRepository {
    fun getProducts(url: String): Single<List<Product>>
}

class ProductsRepositoryImpl(private val mercariApi: MercariApi) : ProductsRepository {

    override fun getProducts(url: String): Single<List<Product>> {
        return mercariApi.getCategoryProducts(url)
    }

}