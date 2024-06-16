package com.github.test.application.domain.dataclasses

sealed class SealedResponse<out T> {
    data class Success<out T>(val data: T) : SealedResponse<T>()
    data class ErrorMessage(val errorMessage: String) : SealedResponse<Nothing>()
}