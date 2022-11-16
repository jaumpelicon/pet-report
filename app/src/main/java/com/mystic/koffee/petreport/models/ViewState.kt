package com.mystic.koffee.petreport.models

sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    data class Error(val error: Throwable) : ViewState<Nothing>()
    data class Success<out T>(val data: T) : ViewState<T>()
}