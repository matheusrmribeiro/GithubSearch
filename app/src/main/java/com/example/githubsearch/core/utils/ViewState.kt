package com.example.githubsearch.core.utils

import androidx.annotation.StringRes

sealed class ViewState<out T> {
    data class Success<T>(val result: T) : ViewState<T>()
    data class Error(val message: String, @StringRes val messageRes: Int? = null) : ViewState<Nothing>()
    object Loading : ViewState<Nothing>()
}
