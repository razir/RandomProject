package com.anton.mercaritest.usecase

import com.anton.mercaritest.data.entity.Category
import com.anton.mercaritest.data.entity.Product
import com.anton.mercaritest.data.source.ProductsRepository
import com.anton.mercaritest.data.usecase.GetProductsUseCase
import com.anton.mercaritest.data.usecase.GetProductsUseCaseImpl
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GetProductsUseCaseTest {

    lateinit var useCase: GetProductsUseCase
    val productsRepository: ProductsRepository = mock()
    val testCategory = Category().apply {
        data = "url"
    }

    @Before
    fun setup() {
        useCase = GetProductsUseCaseImpl(productsRepository)
    }

    @Test
    fun noCategoryUrl() {
        val result = useCase.execute(Category(), false).test()
        result.assertError(Throwable::class.java)
    }

    @Test
    fun forceRefresh_false_emptyCache_loadSuccess() {
        doReturn(emptyList<Product>()).whenever(productsRepository).getCachedProducts(any())
        val products = listOf(Product(), Product())
        doReturn(Single.just(products)).whenever(productsRepository).getProducts(any())

        val result = useCase.execute(testCategory, false).test()
        result.assertResult(products)

        verify(productsRepository).saveProductsCache(products, testCategory.data!!.hashCode().toString())
    }

    @Test
    fun forceRefresh_false_emptyCache_loadFail() {
        doReturn(emptyList<Product>()).whenever(productsRepository).getCachedProducts(any())
        val products = listOf(Product(), Product())
        doReturn(Single.error<List<Product>>(IOException())).whenever(productsRepository).getProducts(any())

        val result = useCase.execute(testCategory, false).test()
        result.assertError(IOException::class.java)

        verify(productsRepository, never()).saveProductsCache(any(), any())
    }

    @Test
    fun forceRefresh_false_nonEmptyCache() {
        val products = listOf(Product(), Product())
        doReturn(products).whenever(productsRepository).getCachedProducts(any())

        val result = useCase.execute(testCategory, false).test()
        result.assertResult(products)

        verify(productsRepository, never()).getProducts(any())
    }

    @Test
    fun forceRefresh_loadSuccess() {
        val products = listOf(Product(), Product())
        doReturn(Single.just(products)).whenever(productsRepository).getProducts(any())

        val result = useCase.execute(testCategory, true).test()
        result.assertResult(products)

        verify(productsRepository).saveProductsCache(products, testCategory.data!!.hashCode().toString())
        verify(productsRepository, never()).getCachedProducts(any())
    }
}