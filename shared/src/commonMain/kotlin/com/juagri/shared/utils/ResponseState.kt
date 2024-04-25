package com.juagri.shared.utils

sealed class ResponseState<out T> {
    data class Loading(val isLoading: Boolean = false): ResponseState<Nothing>()

    data class Success<out T>(
        val data: T
    ): ResponseState<T>()

    data class Error(
        val e: JUError? = JUError.GeneralError
    ): ResponseState<Nothing>()
}

sealed class JUError(message: String): Exception(message){
    data object DeactivatedUserError: JUError(DEACTIVATED_USER)
    data object GeneralError: JUError(GENERAL_ERROR)
    data class CustomError(val error: String): JUError(error)
    data object UnknownError: JUError(UNKNOWN_ERROR)
    companion object{
        const val DEACTIVATED_USER = "User deactivated."
        const val GENERAL_ERROR = "Something went wrong. Please try again later."
        const val UNKNOWN_ERROR = "Unknown Error."
    }
}