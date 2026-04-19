package com.workfort.pstuian.data.model

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * A specialized Result class for Network operations that ensures failures
 * always contain a [NetworkError].
 */
sealed class NetworkResult<out T> {
    data class Success<out T>(val value: T) : NetworkResult<T>()
    data class Failure(val error: NetworkError) : NetworkResult<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isFailure: Boolean get() = this is Failure

    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Failure -> null
    }

    fun exceptionOrNull(): NetworkError? = when (this) {
        is Success -> null
        is Failure -> error
    }

    companion object {
        fun <T> success(value: T): NetworkResult<T> = Success(value)
        fun <T> failure(error: NetworkError): NetworkResult<T> = Failure(error)
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <T> NetworkResult<T>.onSuccess(action: (value: T) -> Unit): NetworkResult<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this is NetworkResult.Success) action(value)
    return this
}

@OptIn(ExperimentalContracts::class)
inline fun <T> NetworkResult<T>.onFailure(action: (error: NetworkError) -> Unit): NetworkResult<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this is NetworkResult.Failure) action(error)
    return this
}
