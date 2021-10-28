package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.remote.Response
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
 *  *
 *  * Last edited by : arhan on 10/1/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
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