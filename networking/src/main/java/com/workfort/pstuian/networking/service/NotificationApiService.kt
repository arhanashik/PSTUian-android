package com.workfort.pstuian.networking.service

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.NotificationEntity
import com.workfort.pstuian.model.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  ****************************************************************************
 *  * Created by : arhan on 01 Oct, 2021 at 1:38 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

interface NotificationApiService {
    @GET(NetworkConst.Remote.Api.Notification.GET_ALL)
    suspend fun getAll(
        @Query(NetworkConst.Params.USER_ID) userId: Int = -1,
        @Query(NetworkConst.Params.USER_TYPE) userType: String = "",
        @Query(NetworkConst.Params.PAGE) page: Int = 1,
        @Query(NetworkConst.Params.LIMIT) limit: Int = 20,
    ): Response<List<NotificationEntity>>
}