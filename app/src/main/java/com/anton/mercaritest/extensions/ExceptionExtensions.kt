package com.anton.mercaritest.extensions

import com.anton.mercaritest.presentation.error.ErrorData
import retrofit2.HttpException

fun Throwable.toErrorData(): ErrorData {
    return when {
        this is HttpException -> ErrorData.Api(this.message())
        else -> ErrorData.Network
    }
}