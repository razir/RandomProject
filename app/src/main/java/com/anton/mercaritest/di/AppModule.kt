package com.anton.mercaritest.di

import com.anton.mercaritest.constant.API_BASE_URL
import com.anton.mercaritest.data.api.MercariApi
import com.anton.mercaritest.data.api.RetrofitClientBuilder
import com.anton.mercaritest.data.source.CategoriesRepository
import com.anton.mercaritest.data.source.CategoriesRepositoryImpl
import com.anton.mercaritest.data.source.ProductsRepository
import com.anton.mercaritest.data.source.ProductsRepositoryImpl
import org.koin.dsl.module.module

val appModule = module {

    single<MercariApi> {
        get<RetrofitClientBuilder>(DI_BASE_RETROFIT_BUILDER).build(API_BASE_URL)
    }

    factory<CategoriesRepository> { CategoriesRepositoryImpl(mercariApi = get()) }

    factory<ProductsRepository> { ProductsRepositoryImpl(mercariApi = get()) }
}