package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.local.blooddonation.BloodDonationEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.remote.Response
import retrofit2.http.*

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

interface BloodDonationApiService {
    @GET(Const.Remote.Api.BloodDonation.GET_ALL)
    suspend fun getAll(
        @Query(Const.Params.USER_ID) userId: Int,
        @Query(Const.Params.USER_TYPE) userType: String,
        @Query(Const.Params.PAGE) page: Int = 1,
        @Query(Const.Params.LIMIT) limit: Int = 20,
    ): Response<List<BloodDonationEntity>>

    @GET(Const.Remote.Api.BloodDonation.GET)
    suspend fun get(@Query(Const.Params.ID) id: Int): Response<BloodDonationEntity>

    @FormUrlEncoded
    @POST(Const.Remote.Api.BloodDonation.INSERT)
    suspend fun insert(
        @Field(Const.Params.USER_ID) userId: Int,
        @Field(Const.Params.USER_TYPE) userType: String,
        @Field(Const.Params.REQUEST_ID) requestId: Int?,
        @Field(Const.Params.DATE) date: String,
        @Field(Const.Params.INFO) info: String?,
    ): Response<BloodDonationEntity>

    @FormUrlEncoded
    @POST(Const.Remote.Api.BloodDonation.UPDATE)
    suspend fun update(
        @Field(Const.Params.ID) id: Int,
        @Field(Const.Params.REQUEST_ID) requestId: Int?,
        @Field(Const.Params.DATE) date: String,
        @Field(Const.Params.INFO) info: String?,
    ): Response<BloodDonationEntity>

    @FormUrlEncoded
    @POST(Const.Remote.Api.BloodDonation.DELETE)
    suspend fun delete(@Field(Const.Params.ID) id: Int): Response<String>
}