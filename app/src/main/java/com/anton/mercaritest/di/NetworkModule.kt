package com.anton.mercaritest.di

import com.anton.mercaritest.data.api.OkHttpClientBuilder
import com.anton.mercaritest.data.api.RetrofitClientBuilder
import com.anton.mercaritest.data.manager.ConnectivityStatusManager
import com.anton.mercaritest.data.manager.ConnectivityStatusManagerImpl
import org.koin.dsl.module.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val DI_GSON_FACTORY = "di_gson_factory"
const val DI_RXJAVA_FACTORY = "di_rxjava_factory"
const val DI_BASE_RETROFIT_BUILDER = "di_base_retrofit_builder"

val networkModule = module {

    single {
        OkHttpClientBuilder().build()
    }

    single<CallAdapter.Factory>(DI_RXJAVA_FACTORY) { RxJava2CallAdapterFactory.create() }

    single<Converter.Factory>(DI_GSON_FACTORY) { GsonConverterFactory.create() }

    single(DI_BASE_RETROFIT_BUILDER) {
        RetrofitClientBuilder(
            client = get(),
            callAdapterFactory = get(DI_RXJAVA_FACTORY),
            converterFactory = get(DI_GSON_FACTORY)
        )
    }

    factory<ConnectivityStatusManager> { ConnectivityStatusManagerImpl(context = get()) }
}