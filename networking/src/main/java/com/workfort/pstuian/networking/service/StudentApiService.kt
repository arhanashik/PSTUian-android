package com.workfort.pstuian.networking.service

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.Response
import com.workfort.pstuian.model.StudentEntity
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 *  ****************************************************************************
 *  * Created by : arhan on 14 Oct, 2021 at 11:45 AM.
 *  * Email : ashik.pstu.cse@gmail.com
 *  *
 *  * This class is for:
 *  * 1.
 *  * 2.
 *  * 3.
 *  ****************************************************************************
 */

interface StudentApiService {
    @GET(NetworkConst.Remote.Api.Student.GET)
    suspend fun get(@Query(NetworkConst.Params.ID) id: Int): Response<StudentEntity>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Student.CHANGE_PROFILE_IMAGE)
    suspend fun changeProfileImage(
        @Field(NetworkConst.Params.ID) id: Int,
        @Field(NetworkConst.Params.IMAGE_URL) imageUrl: String,
    ): Response<String>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Student.UPDATE_NAME)
    suspend fun changeName(
        @Field(NetworkConst.Params.ID) id: Int,
        @Field(NetworkConst.Params.NAME) name: String
    ): Response<String>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Student.UPDATE_BIO)
    suspend fun changeBio(
        @Field(NetworkConst.Params.ID) id: Int,
        @Field(NetworkConst.Params.BIO) bio: String
    ): Response<String>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Student.UPDATE_ACADEMIC_INFO)
    suspend fun changeAcademicInfo(
        @Field(NetworkConst.Params.NAME) name: String,
        @Field(NetworkConst.Params.OLD_ID) oldId: Int,
        @Field(NetworkConst.Params.ID) id: Int,
        @Field(NetworkConst.Params.REG) reg: String,
        @Field(NetworkConst.Params.BLOOD) blood: String,
        @Field(NetworkConst.Params.FACULTY_ID) facultyId: Int,
        @Field(NetworkConst.Params.SESSION) session: String,
        @Field(NetworkConst.Params.BATCH_ID) batchId: Int,
    ): Response<StudentEntity>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Student.UPDATE_CONNECT_INFO)
    suspend fun changeConnectInfo(
        @Field(NetworkConst.Params.ID) id: Int,
        @Field(NetworkConst.Params.ADDRESS) address: String,
        @Field(NetworkConst.Params.PHONE) phone: String,
        @Field(NetworkConst.Params.EMAIL) email: String,
        @Field(NetworkConst.Params.OLD_EMAIL) oldEmail: String,
        @Field(NetworkConst.Params.CV_LINK) cvLink: String,
        @Field(NetworkConst.Params.LINKED_IN) linkedIn: String,
        @Field(NetworkConst.Params.FB_LINK) fbLink: String,
    ): Response<StudentEntity>
}