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

fun <T> ApiResponse<T>.toNetworkResult(): NetworkResult<T> {
    return if (isError || data == null) {
        NetworkResult.failure(NetworkError(responseCode))
    } else {
        NetworkResult.Success(data)
    }
}
