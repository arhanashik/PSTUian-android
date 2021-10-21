package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.remote.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 11:45 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  *
 *  * Last edited by : arhan on 10/14/21.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

interface StudentApiService {
    @FormUrlEncoded
    @POST("student.php?call=updateImageUrl")
    suspend fun changeProfileImage(
        @Field("id") id: Int,
        @Field("image_url") imageUrl: String,
    ): Response<String>

    @FormUrlEncoded
    @POST("student.php?call=updateName")
    suspend fun changeName(
        @Field("id") id: Int,
        @Field("name") name: String
    ): Response<String>
}