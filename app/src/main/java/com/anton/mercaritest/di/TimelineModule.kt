package com.anton.mercaritest.di

import com.anton.mercaritest.constant.API_BASE_URL
import com.anton.mercaritest.data.api.TimelineApi
import com.anton.mercaritest.data.api.RetrofitClientBuilder
import com.anton.mercaritest.data.entity.Category
import com.anton.mercaritest.data.source.CategoriesRepository
import com.anton.mercaritest.data.source.CategoriesRepositoryImpl
import com.anton.mercaritest.data.source.ProductsRepository
import com.anton.mercaritest.data.source.ProductsRepositoryImpl
import com.anton.mercaritest.data.usecase.*
import com.anton.mercaritest.presentation.timeline.TimelineViewModel
import com.anton.mercaritest.presentation.timeline.product.ProductsListViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val timelineModule = module {

    single<TimelineApi> {
        get<RetrofitClientBuilder>(DI_BASE_RETROFIT_BUILDER).build(API_BASE_URL)
    }

    factory<CategoriesRepository> { CategoriesRepositoryImpl(timelineApi = get()) }

    factory<ProductsRepository> { ProductsRepositoryImpl(timelineApi = get(), productsDao = get()) }

    factory<GetCategoriesUseCase> { GetCategoriesUseCaseImpl(categoriesRepository = get()) }

    factory<CleanProductsCacheUseCase> { CleanProductsCacheUseCaseImpl(productsRepository = get()) }

    factory<GetProductsUseCase> { GetProductsUseCaseImpl(productsRepository = get()) }

    viewModel {
        TimelineViewModel(
            getCategoriesUseCase = get(),
            cleanProductsCacheUseCase = get(),
            connectivityStatusManager = get()
        )
    }

    viewModel { (category: Category) ->
        ProductsListViewModel(
            getProductsUseCase = get(),
            connectivityStatusManager = get(),
            category = category
        )
    }
}