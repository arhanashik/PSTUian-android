package com.workfort.pstuian.networking.service

import com.workfort.pstuian.model.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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

interface SupportApiService {
    @FormUrlEncoded
    @POST("user_query.php?call=add")
    suspend fun sendInquiry(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("type") type: String,
        @Field("query") query: String
    ): Response<String>
}