package com.anton.mercaritest.data.api

import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit

class RetrofitClientBuilder(
    val client: OkHttpClient,
    val callAdapterFactory: CallAdapter.Factory,
    val converterFactory: Converter.Factory
) {

    inline fun <reified T> build(baseUrl: String): T {
        return Retrofit.Builder()
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(converterFactory)
            .client(client)
            .baseUrl(baseUrl)
            .build()
            .create(T::class.java)
    }
}