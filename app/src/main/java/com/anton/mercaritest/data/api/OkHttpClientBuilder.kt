package com.anton.mercaritest.data.api

import com.anton.mercaritest.BuildConfig
import com.anton.mercaritest.constant.HTTP_TIMEOUT_SEC
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class OkHttpClientBuilder {

    fun build(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(HTTP_TIMEOUT_SEC, TimeUnit.SECONDS)
            .connectTimeout(HTTP_TIMEOUT_SEC, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(logging)
                }
            }
            .build()
    }
}