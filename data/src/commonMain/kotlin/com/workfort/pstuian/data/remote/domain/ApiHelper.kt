package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.NetworkError
import com.workfort.pstuian.data.model.NetworkErrorCode
import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.NetworkConst

abstract class ApiHelper<T> {
    open suspend fun getAll(): NetworkResult<List<T>> =
        NetworkResult.failure(NetworkError(NetworkErrorCode.UNKNOWN))

    open suspend fun getAll(
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<T>> = NetworkResult.failure(NetworkError(NetworkErrorCode.UNKNOWN))

    open suspend fun get(id: Int): NetworkResult<T> =
        NetworkResult.failure(NetworkError(NetworkErrorCode.UNKNOWN))

    open suspend fun insert(item: T): NetworkResult<T> =
        NetworkResult.failure(NetworkError(NetworkErrorCode.UNKNOWN))

    open suspend fun update(item: T): NetworkResult<Unit> =
        NetworkResult.failure(NetworkError(NetworkErrorCode.UNKNOWN))

    open suspend fun delete(id: Int): NetworkResult<Unit> =
        NetworkResult.failure(NetworkError(NetworkErrorCode.UNKNOWN))

    open suspend fun search(
        query: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE,
    ): NetworkResult<List<T>> = NetworkResult.failure(NetworkError(NetworkErrorCode.UNKNOWN))
}