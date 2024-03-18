package com.workfort.pstuian.networking

import com.workfort.pstuian.appconstant.NetworkConst

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 10:01 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

abstract class ApiHelper<T> {
    open suspend fun getAll(): List<T> = emptyList()
    open suspend fun getAll(
        page: Int,
        limit: Int = NetworkConst.Params.Default.PAGE_SIZE
    ): List<T> = emptyList()
    open suspend fun get(id: Int): T = throw Exception("Not implemented yet")
    open suspend fun insert(item: T): Int = 0
    open suspend fun update(item: T): T = throw Exception("Not implemented yet")
    open suspend fun delete(id: Int): Boolean = false
}