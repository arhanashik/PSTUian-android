package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.local.checkinlocation.CheckInLocationEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.remote.Response
import retrofit2.http.*

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Dec, 2021 at 21:19.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 2021/12/14.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

interface CheckInLocationApiService {
    @GET(Const.Remote.Api.CheckInLocation.GET_ALL)
    suspend fun getAll(
        @Query(Const.Params.PAGE) page: Int = 1,
        @Query(Const.Params.LIMIT) limit: Int = 20,
    ): Response<List<CheckInLocationEntity>>

    @GET(Const.Remote.Api.CheckInLocation.GET)
    suspend fun get(@Query(Const.Params.ID) id: Int):
            Response<CheckInLocationEntity>

    @GET(Const.Remote.Api.CheckInLocation.SEARCH)
    suspend fun search(
        @Query(Const.Params.QUERY) query: String,
        @Query(Const.Params.PAGE) page: Int = 1,
        @Query(Const.Params.LIMIT) limit: Int = 20,
    ): Response<List<CheckInLocationEntity>>

    @FormUrlEncoded
    @POST(Const.Remote.Api.CheckInLocation.INSERT)
    suspend fun insert(
        @Field(Const.Params.USER_ID) userId: Int,
        @Field(Const.Params.USER_TYPE) userType: String,
        @Field(Const.Params.NAME) name: String,
        @Field(Const.Params.DETAILS) details: String? = "",
        @Field(Const.Params.IMAGE_URL) imageUrl: String? = "",
        @Field(Const.Params.LINK) link: String? = "",
    ): Response<CheckInLocationEntity>
}