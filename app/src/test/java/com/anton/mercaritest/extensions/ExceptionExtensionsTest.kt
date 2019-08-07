package com.anton.mercaritest.extensions

import com.anton.mercaritest.presentation.error.ErrorData
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

class ExceptionExtensionsTest {

    @Test
    fun testHTTPException() {
        val e: HttpException = mock()
        doReturn("http error").whenever(e).message()
        val errorData = e.toErrorData()
        assertEquals("http error", (errorData as ErrorData.Api).message)
    }

    @Test
    fun testIOException() {
        val errorData = IOException().toErrorData()
        assertEquals(ErrorData.Network, errorData)
    }
}