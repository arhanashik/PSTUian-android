package com.workfort.pstuian.networking.service

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.BloodDonationRequestEntity
import com.workfort.pstuian.model.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
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

interface BloodDonationRequestApiService {
    @GET(NetworkConst.Remote.Api.BloodDonationRequest.GET_ALL)
    suspend fun getAll(
        @Query(NetworkConst.Params.PAGE) page: Int = 1,
        @Query(NetworkConst.Params.LIMIT) limit: Int = 20,
    ): Response<List<BloodDonationRequestEntity>>

    @GET(NetworkConst.Remote.Api.BloodDonationRequest.GET)
    suspend fun get(@Query(NetworkConst.Params.ID) id: Int): Response<BloodDonationRequestEntity>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.BloodDonationRequest.INSERT)
    suspend fun insert(
        @Field(NetworkConst.Params.USER_ID) userId: Int,
        @Field(NetworkConst.Params.USER_TYPE) userType: String,
        @Field(NetworkConst.Params.BLOOD_GROUP) bloodGroup: String,
        @Field(NetworkConst.Params.BEFORE_DATE) beforeDate: String,
        @Field(NetworkConst.Params.CONTACT) contact: String,
        @Field(NetworkConst.Params.INFO) info: String?,
    ): Response<BloodDonationRequestEntity>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.BloodDonationRequest.UPDATE)
    suspend fun update(
        @Field(NetworkConst.Params.ID) id: Int,
        @Field(NetworkConst.Params.BLOOD_GROUP) bloodGroup: String,
        @Field(NetworkConst.Params.BEFORE_DATE) beforeDate: String,
        @Field(NetworkConst.Params.CONTACT) contact: String,
        @Field(NetworkConst.Params.INFO) info: String,
    ): Response<BloodDonationRequestEntity>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.BloodDonationRequest.DELETE)
    suspend fun delete(@Field(NetworkConst.Params.ID) id: Int): Response<String>
}