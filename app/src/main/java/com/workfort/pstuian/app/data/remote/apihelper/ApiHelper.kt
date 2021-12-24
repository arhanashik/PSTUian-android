package com.workfort.pstuian.app.data.remote.apihelper

import com.workfort.pstuian.app.data.local.constant.Const

/**
 *  ****************************************************************************
 *  * Created by : arhan on 30 Sep, 2021 at 10:01 PM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1. The base activity of each activity
 *  * 2. Set layout, toolbar, onClickListeners for the activities
 *  * 3.
 *  *
 *  * Last edited by : arhan on 9/30/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

abstract class ApiHelper<T> {
    open suspend fun getAll(): List<T> = emptyList()
    open suspend fun getAll(
        page: Int,
        limit: Int = Const.Params.Default.PAGE_SIZE
    ): List<T> = emptyList()
    open suspend fun get(id: Int): T = throw Exception("Not implemented yet")
    open suspend fun insert(item: T): Int = 0
    open suspend fun update(item: T): T = throw Exception("Not implemented yet")
    open suspend fun delete(id: Int): Boolean = false
}