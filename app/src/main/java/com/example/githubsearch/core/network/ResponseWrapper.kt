package com.example.githubsearch.core.network

sealed class ResponseWrapper<T>(
    val result: T? = null,
    val message: String? = null,
    val status: Int? = null
) {
    class Success<T>(result: T, status: Int? = null) : ResponseWrapper<T>(
        result = result,
        status = status
    )

    class Error<T>(
        message: String,
        data: T? = null,
        status: Int? = null
    ) : ResponseWrapper<T>(data, message, status)
}