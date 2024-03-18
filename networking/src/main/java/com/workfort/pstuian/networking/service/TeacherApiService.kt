package com.workfort.pstuian.networking.service

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.Response
import com.workfort.pstuian.model.TeacherEntity
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

interface TeacherApiService {
    @GET(NetworkConst.Remote.Api.Teacher.GET)
    suspend fun get(@Query(NetworkConst.Params.ID) id: Int): Response<TeacherEntity>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Teacher.CHANGE_PROFILE_IMAGE)
    suspend fun changeProfileImage(
        @Field(NetworkConst.Params.ID) id: Int,
        @Field(NetworkConst.Params.IMAGE_URL) imageUrl: String,
    ): Response<String>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Teacher.UPDATE_NAME)
    suspend fun changeName(
        @Field(NetworkConst.Params.ID) id: Int,
        @Field(NetworkConst.Params.NAME) name: String
    ): Response<String>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Teacher.UPDATE_BIO)
    suspend fun changeBio(
        @Field(NetworkConst.Params.ID) id: Int,
        @Field(NetworkConst.Params.BIO) bio: String
    ): Response<String>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Teacher.UPDATE_ACADEMIC_INFO)
    suspend fun changeAcademicInfo(
        @Field(NetworkConst.Params.ID) id: Int,
        @Field(NetworkConst.Params.NAME) name: String,
        @Field(NetworkConst.Params.DESIGNATION) designation: String,
        @Field(NetworkConst.Params.DEPARTMENT) reg: String,
        @Field(NetworkConst.Params.BLOOD) blood: String,
        @Field(NetworkConst.Params.FACULTY_ID) facultyId: Int,
    ): Response<TeacherEntity>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Teacher.UPDATE_CONNECT_INFO)
    suspend fun changeConnectInfo(
        @Field(NetworkConst.Params.ID) id: Int,
        @Field(NetworkConst.Params.ADDRESS) address: String,
        @Field(NetworkConst.Params.PHONE) phone: String,
        @Field(NetworkConst.Params.EMAIL) email: String,
        @Field(NetworkConst.Params.OLD_EMAIL) oldEmail: String,
        @Field(NetworkConst.Params.LINKED_IN) linkedIn: String,
        @Field(NetworkConst.Params.FB_LINK) fbLink: String,
    ): Response<TeacherEntity>
}