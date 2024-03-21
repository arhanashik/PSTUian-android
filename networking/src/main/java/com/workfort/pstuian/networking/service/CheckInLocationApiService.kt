package com.workfort.pstuian.networking.service

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.CheckInLocationEntity
import com.workfort.pstuian.model.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Dec, 2021 at 21:19.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

interface CheckInLocationApiService {
    @GET(NetworkConst.Remote.Api.CheckInLocation.GET_ALL)
    suspend fun getAll(
        @Query(NetworkConst.Params.PAGE) page: Int = 1,
        @Query(NetworkConst.Params.LIMIT) limit: Int = 20,
    ): Response<List<CheckInLocationEntity>>

    @GET(NetworkConst.Remote.Api.CheckInLocation.GET)
    suspend fun get(@Query(NetworkConst.Params.ID) id: Int):
            Response<CheckInLocationEntity>

    @GET(NetworkConst.Remote.Api.CheckInLocation.SEARCH)
    suspend fun search(
        @Query(NetworkConst.Params.QUERY) query: String,
        @Query(NetworkConst.Params.PAGE) page: Int = 1,
        @Query(NetworkConst.Params.LIMIT) limit: Int = 20,
    ): Response<List<CheckInLocationEntity>>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.CheckInLocation.INSERT)
    suspend fun insert(
        @Field(NetworkConst.Params.USER_ID) userId: Int,
        @Field(NetworkConst.Params.USER_TYPE) userType: String,
        @Field(NetworkConst.Params.NAME) name: String,
        @Field(NetworkConst.Params.DETAILS) details: String? = "",
        @Field(NetworkConst.Params.IMAGE_URL) imageUrl: String? = "",
        @Field(NetworkConst.Params.LINK) link: String? = "",
    ): Response<CheckInLocationEntity>
}