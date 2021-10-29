package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.notification.NotificationEntity
import com.workfort.pstuian.app.data.remote.Response
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
 *  *
 *  * Last edited by : arhan on 10/1/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

interface NotificationApiService {
    @GET(Const.Remote.Api.Notification.GET_ALL)
    suspend fun getAll(
        @Query(Const.Params.USER_ID) userId: Int = 0
    ): Response<List<NotificationEntity>>
}