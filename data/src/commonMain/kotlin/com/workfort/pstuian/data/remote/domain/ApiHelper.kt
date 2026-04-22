package com.workfort.pstuian.data.remote.domain

import com.workfort.pstuian.data.model.NetworkResult
import com.workfort.pstuian.data.remote.NetworkConst

abstract class ApiHelper<T> {
    open suspend fun getAll(): NetworkResult<List<T>> = NetworkResult.success(emptyList())
    open suspend fun getAll(
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE
    ): NetworkResult<List<T>> = NetworkResult.success(emptyList())
    open suspend fun get(id: Int): T = throw Exception("Not implemented yet")
    open suspend fun insert(item: T): Int = 0
    open suspend fun update(item: T): T = throw Exception("Not implemented yet")
    open suspend fun delete(id: Int): Boolean = false
    open suspend fun search(
        query: String,
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE
    ): List<T> = emptyList()
}