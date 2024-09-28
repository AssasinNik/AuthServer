package com.example.utils
import io.ktor.http.*

sealed class Response<T>(val statusCode: HttpStatusCode = HttpStatusCode.OK) {
    data class SuccessResponse<T>(
        val data: T? = null,
        val message: String? = null
    ): Response<T>()

    data class ErrorResponse<T>(
        val exception: T? = null,
        val message: String? = null
    ): Response <T>()
}