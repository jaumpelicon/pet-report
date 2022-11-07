package com.mystic.koffee.petreport.models

interface ViewState {
    object Loading : ViewState
    data class Error(val error: Throwable) : ViewState
    data class Success<out T>(val data: T) : ViewState
}