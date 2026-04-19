package com.workfort.pstuian.featuredomain.model

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * A specialized Result class for Network operations that ensures failures
 * always contain a [DomainResult].
 */
sealed class DomainResult<out T> {
    data class Success<out T>(val value: T) : DomainResult<T>()
    data class Failure(val error: DomainError) : DomainResult<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isFailure: Boolean get() = this is Failure

    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Failure -> null
    }

    fun exceptionOrNull(): DomainError? = when (this) {
        is Success -> null
        is Failure -> error
    }

    companion object {
        fun <T> success(value: T): DomainResult<T> = Success(value)
        fun <T> failure(error: DomainError): DomainResult<T> = Failure(error)
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <T> DomainResult<T>.onSuccess(action: (value: T) -> Unit): DomainResult<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this is DomainResult.Success) action(value)
    return this
}

@OptIn(ExperimentalContracts::class)
inline fun <T> DomainResult<T>.onFailure(action: (error: DomainError) -> Unit): DomainResult<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this is DomainResult.Failure) action(error)
    return this
}

/**
 * Returns the encapsulated value if this instance represents [Success][DomainResult.Success] or the
 * result of [onFailure] function for the encapsulated [DomainError] if it is [Failure][DomainResult.Failure].
 */
@OptIn(ExperimentalContracts::class)
inline fun <R, T : R> DomainResult<T>.getOrElse(onFailure: (error: DomainError) -> R): R {
    contract {
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is DomainResult.Success -> value
        is DomainResult.Failure -> onFailure(error)
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <R, T> DomainResult<T>.map(transform: (value: T) -> R): DomainResult<R> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is DomainResult.Success -> DomainResult.success(transform(value))
        is DomainResult.Failure -> DomainResult.failure(error)
    }
}
