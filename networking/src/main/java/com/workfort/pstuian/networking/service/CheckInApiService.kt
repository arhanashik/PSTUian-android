package com.workfort.pstuian.networking.service

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.CheckInEntity
import com.workfort.pstuian.model.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 *  ****************************************************************************
 *  * Created by : arhan on 13 Dec, 2021 at 11:58 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

interface CheckInApiService {
    @GET(NetworkConst.Remote.Api.CheckIn.GET_ALL)
    suspend fun getAll(
        @Query(NetworkConst.Params.LOCATION_ID) locationId: Int,
        @Query(NetworkConst.Params.PAGE) page: Int = 1,
        @Query(NetworkConst.Params.LIMIT) limit: Int = 20,
    ): Response<List<CheckInEntity>>

    /**
     * Get the check in list for specific user
     * */
    @GET(NetworkConst.Remote.Api.CheckIn.GET_ALL)
    suspend fun getAll(
        @Query(NetworkConst.Params.USER_ID) userId: Int,
        @Query(NetworkConst.Params.USER_TYPE) userType: String,
        @Query(NetworkConst.Params.PAGE) page: Int = 1,
        @Query(NetworkConst.Params.LIMIT) limit: Int = 20,
    ): Response<List<CheckInEntity>>

    @GET(NetworkConst.Remote.Api.CheckIn.GET)
    suspend fun get(
        @Query(NetworkConst.Params.USER_ID) userId: Int,
        @Query(NetworkConst.Params.USER_TYPE) userType: String
    ): Response<CheckInEntity>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.CheckIn.CHECK_IN)
    suspend fun checkIn(
        @Field(NetworkConst.Params.LOCATION_ID) locationId: Int,
        @Field(NetworkConst.Params.USER_ID) userId: Int,
        @Field(NetworkConst.Params.USER_TYPE) userType: String
    ): Response<CheckInEntity>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.CheckIn.PRIVACY)
    suspend fun updatePrivacy(
        @Field(NetworkConst.Params.ID) id: Int,
        @Field(NetworkConst.Params.PRIVACY) privacy: String,
    ): Response<CheckInEntity>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.CheckIn.DELETE)
    suspend fun delete(@Field(NetworkConst.Params.ID) id: Int): Response<Int>
}