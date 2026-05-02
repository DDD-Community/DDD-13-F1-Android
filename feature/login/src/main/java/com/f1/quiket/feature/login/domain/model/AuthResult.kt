package com.f1.quiket.feature.login.domain.model

sealed interface AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>

    data class Failure(
        val code: String,
        val message: String,
        val httpCode: Int? = null,
        val fieldErrors: List<FieldError> = emptyList(),
        val cause: Throwable? = null,
    ) : AuthResult<Nothing>
}

data class FieldError(
    val field: String,
    val reason: String,
)
