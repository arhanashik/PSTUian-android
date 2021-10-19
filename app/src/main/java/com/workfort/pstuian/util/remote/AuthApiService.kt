package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.local.config.ConfigEntity
import com.workfort.pstuian.app.data.local.student.StudentEntity
import com.workfort.pstuian.app.data.remote.AuthResponse
import com.workfort.pstuian.app.data.remote.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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

interface AuthApiService {
    @GET("config.php?call=getLatest")
    suspend fun getConfig(): Response<ConfigEntity>

    @FormUrlEncoded
    @POST("auth.php?call=signIn")
    suspend fun signIn(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("user_type") userType: String,
    ): AuthResponse<StudentEntity>

    @FormUrlEncoded
    @POST("auth.php?call=signUp")
    suspend fun signUp(
        @Field("name") name: String,
        @Field("id") id: String,
        @Field("reg") reg: String,
        @Field("faculty_id") facultyId: Int,
        @Field("batch_id") batchId: Int,
        @Field("session") session: String
    ): AuthResponse<StudentEntity>

    @FormUrlEncoded
    @POST("auth.php?call=signOut")
    suspend fun signOut(
        @Field("id") id: Int,
        @Field("user_type") userType: String,
    ): AuthResponse<String>
}