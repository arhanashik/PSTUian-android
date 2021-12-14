package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.local.checkin.CheckInEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.remote.Response
import retrofit2.http.*

/**
 *  ****************************************************************************
 *  * Created by : arhan on 13 Dec, 2021 at 11:58 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 12/13/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

interface CheckInApiService {
    @GET(Const.Remote.Api.CheckIn.GET_ALL)
    suspend fun getAll(
        @Query(Const.Params.LOCATION_ID) locationId: Int,
        @Query(Const.Params.PAGE) page: Int = 1,
        @Query(Const.Params.LIMIT) limit: Int = 20,
    ): Response<List<CheckInEntity>>

    /**
     * Get the check in list for specific user
     * */
    @GET(Const.Remote.Api.CheckIn.GET_ALL)
    suspend fun getAll(
        @Query(Const.Params.USER_ID) userId: Int,
        @Query(Const.Params.USER_TYPE) userType: String,
        @Query(Const.Params.PAGE) page: Int = 1,
        @Query(Const.Params.LIMIT) limit: Int = 20,
    ): Response<List<CheckInEntity>>

    @GET(Const.Remote.Api.CheckIn.GET)
    suspend fun get(
        @Query(Const.Params.USER_ID) userId: Int,
        @Query(Const.Params.USER_TYPE) userType: String
    ): Response<CheckInEntity>

    @FormUrlEncoded
    @POST(Const.Remote.Api.CheckIn.CHECK_IN)
    suspend fun checkIn(
        @Field(Const.Params.LOCATION_ID) locationId: Int,
        @Field(Const.Params.USER_ID) userId: Int,
        @Field(Const.Params.USER_TYPE) userType: String
    ): Response<CheckInEntity>

    @FormUrlEncoded
    @POST(Const.Remote.Api.CheckIn.VISIBILITY)
    suspend fun updateVisibility(
        @Field(Const.Params.USER_ID) userId: Int,
        @Field(Const.Params.USER_TYPE) userType: String,
        @Field(Const.Params.VISIBILITY) visibility: String,
    ): Response<CheckInEntity>
}