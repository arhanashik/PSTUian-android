package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.teacher.TeacherEntity
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

interface TeacherApiService {
    @FormUrlEncoded
    @POST(Const.Remote.Api.Teacher.CHANGE_PROFILE_IMAGE)
    suspend fun changeProfileImage(
        @Field(Const.Params.ID) id: Int,
        @Field(Const.Params.IMAGE_URL) imageUrl: String,
    ): Response<String>

    @FormUrlEncoded
    @POST(Const.Remote.Api.Teacher.UPDATE_NAME)
    suspend fun changeName(
        @Field(Const.Params.ID) id: Int,
        @Field(Const.Params.NAME) name: String
    ): Response<String>

    @FormUrlEncoded
    @POST(Const.Remote.Api.Teacher.UPDATE_BIO)
    suspend fun changeBio(
        @Field(Const.Params.ID) id: Int,
        @Field(Const.Params.BIO) bio: String
    ): Response<String>

    @FormUrlEncoded
    @POST(Const.Remote.Api.Teacher.UPDATE_ACADEMIC_INFO)
    suspend fun changeAcademicInfo(
        @Field(Const.Params.ID) id: Int,
        @Field(Const.Params.NAME) name: String,
        @Field(Const.Params.DESIGNATION) designation: String,
        @Field(Const.Params.DEPARTMENT) reg: String,
        @Field(Const.Params.BLOOD) blood: String,
        @Field(Const.Params.FACULTY_ID) facultyId: Int,
    ): Response<TeacherEntity>

    @FormUrlEncoded
    @POST(Const.Remote.Api.Teacher.UPDATE_CONNECT_INFO)
    suspend fun changeConnectInfo(
        @Field(Const.Params.ID) id: Int,
        @Field(Const.Params.ADDRESS) address: String,
        @Field(Const.Params.PHONE) phone: String,
        @Field(Const.Params.EMAIL) email: String,
        @Field(Const.Params.OLD_EMAIL) oldEmail: String,
        @Field(Const.Params.LINKED_IN) linkedIn: String,
        @Field(Const.Params.FB_LINK) fbLink: String,
    ): Response<TeacherEntity>
}