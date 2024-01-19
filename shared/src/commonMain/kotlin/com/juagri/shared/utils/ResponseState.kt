package com.juagri.shared.utils

sealed class ResponseState<out T> {
    data class Loading(val isLoading: Boolean = false): ResponseState<Nothing>()

    data class Success<out T>(
        val data: T
    ): ResponseState<T>()

    data class Error(
        val e: Exception?
    ): ResponseState<Nothing>()
}