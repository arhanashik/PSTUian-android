package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.local.student.StudentEntity
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

    @FormUrlEncoded
    @POST("student.php?call=updateAcademicInfo")
    suspend fun changeAcademicInfo(
        @Field("old_id") oldId: Int,
        @Field("id") id: Int,
        @Field("reg") reg: String,
        @Field("blood") blood: String,
        @Field("faculty_id") facultyId: Int,
        @Field("session") session: String,
        @Field("batch_id") batchId: Int,
    ): Response<StudentEntity>

    @FormUrlEncoded
    @POST("student.php?call=updateConnectInfo")
    suspend fun changeConnectInfo(
        @Field("id") id: Int,
        @Field("address") address: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("old_email") oldEmail: String,
        @Field("cv_link") cvLink: String,
        @Field("linked_in") linkedIn: String,
        @Field("fb_link") fbLink: String,
    ): Response<StudentEntity>
}