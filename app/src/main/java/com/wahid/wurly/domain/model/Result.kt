package com.wahid.wurly.domain.model

sealed interface Result<T> {

    data class Success<T>(val data:T): Result<T>
    data class Failure(val error: Throwable): Result<Throwable>

}