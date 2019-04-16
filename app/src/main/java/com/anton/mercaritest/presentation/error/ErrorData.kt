package com.anton.mercaritest.presentation.error

import android.content.Context
import com.anton.mercaritest.R

sealed class ErrorData {
    object Network : ErrorData()
    class Api(val message: String?) : ErrorData()

    fun getLongMsg(context: Context): String? {
        return when (this) {
            is Network -> context.getString(R.string.errorNoConnectionLong)
            is Api -> message
        }
    }

    fun getShortMsg(context: Context): String {
        return when (this) {
            is Network -> context.getString(R.string.errorNoConnectionShort)
            is Api -> context.getString(R.string.errorApiShort)
        }
    }
}