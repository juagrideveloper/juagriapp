package com.juagri.shared.utils

sealed class UIState<out T> {
   data class Loading(val isLoading: Boolean = false) : UIState<Nothing>()
    data class Success<T>(val data: T) : UIState<T>()
    data class Error(val error: String) : UIState<Nothing>()
}