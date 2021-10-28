package com.workfort.pstuian.util.remote

import com.workfort.pstuian.app.data.local.config.ConfigEntity
import com.workfort.pstuian.app.data.local.constant.Const
import com.workfort.pstuian.app.data.local.device.DeviceEntity
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
    @GET(Const.Remote.Api.GET_CONFIG)
    suspend fun getConfig(): Response<ConfigEntity>

    @FormUrlEncoded
    @POST(Const.Remote.Api.REGISTER_DEVICE)
    suspend fun registerDevice(
        @Field(Const.Params.ID) id: String,
        @Field(Const.Params.FCM_TOKEN) fcmToken: String,
        @Field(Const.Params.MODEL) model: String,
        @Field(Const.Params.ANDROID_VERSION) androidVersion: String,
        @Field(Const.Params.APP_VERSION_CODE) appVersionCode: Int,
        @Field(Const.Params.APP_VERSION_NAME) appVersionName: String,
        @Field(Const.Params.IP_ADDRESS) ipAddress: String,
        @Field(Const.Params.LAT) lat: String,
        @Field(Const.Params.LNG) lng: String,
        @Field(Const.Params.LOCALE) locale: String
    ): Response<DeviceEntity>

    @FormUrlEncoded
    @POST(Const.Remote.Api.UPDATE_FCM_TOKEN)
    fun updateFcmToken(
        @Field(Const.Params.DEVICE_ID) deviceId: String,
        @Field(Const.Params.FCM_TOKEN) fcmToken: String
    ): Response<DeviceEntity>

    @FormUrlEncoded
    @POST(Const.Remote.Api.Auth.SIGN_IN)
    suspend fun signIn(
        @Field(Const.Params.EMAIL) email: String,
        @Field(Const.Params.PASSWORD) password: String,
        @Field(Const.Params.USER_TYPE) userType: String,
        @Field(Const.Params.DEVICE_ID) deviceId: String
    ): AuthResponse<StudentEntity>

    @FormUrlEncoded
    @POST(Const.Remote.Api.Auth.SIGN_UP)
    suspend fun signUp(
        @Field(Const.Params.NAME) name: String,
        @Field(Const.Params.ID) id: String,
        @Field(Const.Params.REG) reg: String,
        @Field(Const.Params.FACULTY_ID) facultyId: Int,
        @Field(Const.Params.BATCH_ID) batchId: Int,
        @Field(Const.Params.SESSION) session: String,
        @Field(Const.Params.DEVICE_ID) deviceId: String
    ): AuthResponse<StudentEntity>

    @FormUrlEncoded
    @POST(Const.Remote.Api.Auth.SIGN_OUT)
    suspend fun signOut(
        @Field(Const.Params.ID) id: Int,
        @Field(Const.Params.USER_TYPE) userType: String,
    ): AuthResponse<String>
}