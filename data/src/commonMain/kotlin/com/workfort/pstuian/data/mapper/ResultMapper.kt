package com.workfort.pstuian.data.mapper

import com.workfort.pstuian.data.model.ApiResponse
import com.workfort.pstuian.data.model.NetworkError
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.featuredomain.model.DomainResult

fun <T> NetworkResult<T>.toDomainResult(domainErrorMapper: DomainErrorMapper): DomainResult<T> {
    return when (this) {
        is NetworkResult.Success -> DomainResult.success(value)
        is NetworkResult.Failure -> DomainResult.failure(
            error = domainErrorMapper.map(networkError = error),
        )
    }
}

/**
 * Maps an [ApiResponse] to [NetworkResult]:
 * - For [ApiResponse]<[Unit]> (reified [T] == [Unit]): success iff [ApiResponse.isSuccess]; value is always [Unit].
 * - Otherwise: success iff [ApiResponse.isSuccess] and [ApiResponse.data] is non-null; value is [ApiResponse.data].
 */
inline fun <reified T> ApiResponse<T>.toNetworkResult(): NetworkResult<T> {
    if (isError) {
        return NetworkResult.failure(NetworkError(responseCode))
    }
    return if (T::class == Unit::class) {
        NetworkResult.Success(Unit as T)
    } else {
        if (data == null) {
            NetworkResult.failure(NetworkError(responseCode))
        } else {
            NetworkResult.Success(data)
        }
    }
}
