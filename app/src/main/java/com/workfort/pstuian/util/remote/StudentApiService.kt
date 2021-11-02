package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.local.constant.Const
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
    @POST(Const.Remote.Api.Student.CHANGE_PROFILE_IMAGE)
    suspend fun changeProfileImage(
        @Field(Const.Params.ID) id: Int,
        @Field(Const.Params.IMAGE_URL) imageUrl: String,
    ): Response<String>

    @FormUrlEncoded
    @POST(Const.Remote.Api.Student.UPDATE_NAME)
    suspend fun changeName(
        @Field(Const.Params.ID) id: Int,
        @Field(Const.Params.NAME) name: String
    ): Response<String>

    @FormUrlEncoded
    @POST(Const.Remote.Api.Student.UPDATE_BIO)
    suspend fun changeBio(
        @Field(Const.Params.ID) id: Int,
        @Field(Const.Params.BIO) bio: String
    ): Response<String>

    @FormUrlEncoded
    @POST(Const.Remote.Api.Student.UPDATE_ACADEMIC_INFO)
    suspend fun changeAcademicInfo(
        @Field(Const.Params.NAME) name: String,
        @Field(Const.Params.OLD_ID) oldId: Int,
        @Field(Const.Params.ID) id: Int,
        @Field(Const.Params.REG) reg: String,
        @Field(Const.Params.BLOOD) blood: String,
        @Field(Const.Params.FACULTY_ID) facultyId: Int,
        @Field(Const.Params.SESSION) session: String,
        @Field(Const.Params.BATCH_ID) batchId: Int,
    ): Response<StudentEntity>

    @FormUrlEncoded
    @POST(Const.Remote.Api.Student.UPDATE_CONNECT_INFO)
    suspend fun changeConnectInfo(
        @Field(Const.Params.ID) id: Int,
        @Field(Const.Params.ADDRESS) address: String,
        @Field(Const.Params.PHONE) phone: String,
        @Field(Const.Params.EMAIL) email: String,
        @Field(Const.Params.OLD_EMAIL) oldEmail: String,
        @Field(Const.Params.CV_LINK) cvLink: String,
        @Field(Const.Params.LINKED_IN) linkedIn: String,
        @Field(Const.Params.FB_LINK) fbLink: String,
    ): Response<StudentEntity>
}