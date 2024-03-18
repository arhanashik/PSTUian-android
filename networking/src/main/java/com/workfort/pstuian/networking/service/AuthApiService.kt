package com.workfort.pstuian.networking.service

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.AuthResponse
import com.workfort.pstuian.model.ConfigEntity
import com.workfort.pstuian.model.DeviceEntity
import com.workfort.pstuian.model.Response
import com.workfort.pstuian.model.StudentEntity
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

interface AuthApiService {
    @GET(NetworkConst.Remote.Api.GET_CONFIG)
    suspend fun getConfig(): Response<ConfigEntity>

    @GET(NetworkConst.Remote.Api.Device.GET_ALL)
    suspend fun getAllDevices(
        @Query(NetworkConst.Params.USER_ID) userId: Int,
        @Query(NetworkConst.Params.USER_TYPE) userType: String,
        @Query(NetworkConst.Params.DEVICE_ID) deviceId: String,
        @Query(NetworkConst.Params.PAGE) page: Int,
        @Query(NetworkConst.Params.LIMIT) limit: Int,
    ): Response<List<DeviceEntity>>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Device.REGISTER)
    suspend fun registerDevice(
        @Field(NetworkConst.Params.ID) id: String,
        @Field(NetworkConst.Params.FCM_TOKEN) fcmToken: String,
        @Field(NetworkConst.Params.MODEL) model: String,
        @Field(NetworkConst.Params.ANDROID_VERSION) androidVersion: String,
        @Field(NetworkConst.Params.APP_VERSION_CODE) appVersionCode: Int,
        @Field(NetworkConst.Params.APP_VERSION_NAME) appVersionName: String,
        @Field(NetworkConst.Params.IP_ADDRESS) ipAddress: String,
        @Field(NetworkConst.Params.LAT) lat: String,
        @Field(NetworkConst.Params.LNG) lng: String,
        @Field(NetworkConst.Params.LOCALE) locale: String
    ): Response<DeviceEntity>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Device.UPDATE_FCM_TOKEN)
    fun updateFcmToken(
        @Field(NetworkConst.Params.DEVICE_ID) deviceId: String,
        @Field(NetworkConst.Params.FCM_TOKEN) fcmToken: String
    ): Response<DeviceEntity>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Auth.SIGN_IN)
    suspend fun signInStudent(
        @Field(NetworkConst.Params.EMAIL) email: String,
        @Field(NetworkConst.Params.PASSWORD) password: String,
        @Field(NetworkConst.Params.DEVICE_ID) deviceId: String,
        @Field(NetworkConst.Params.USER_TYPE) userType: String = NetworkConst.Params.UserType.STUDENT,
    ): AuthResponse<StudentEntity>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Auth.SIGN_IN)
    suspend fun signInTeacher(
        @Field(NetworkConst.Params.EMAIL) email: String,
        @Field(NetworkConst.Params.PASSWORD) password: String,
        @Field(NetworkConst.Params.DEVICE_ID) deviceId: String,
        @Field(NetworkConst.Params.USER_TYPE) userType: String = NetworkConst.Params.UserType.TEACHER,
    ): AuthResponse<TeacherEntity>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Auth.SIGN_UP_STUDENT)
    suspend fun signUpStudent(
        @Field(NetworkConst.Params.NAME) name: String,
        @Field(NetworkConst.Params.ID) id: String,
        @Field(NetworkConst.Params.REG) reg: String,
        @Field(NetworkConst.Params.FACULTY_ID) facultyId: Int,
        @Field(NetworkConst.Params.BATCH_ID) batchId: Int,
        @Field(NetworkConst.Params.SESSION) session: String,
        @Field(NetworkConst.Params.EMAIL) email: String,
        @Field(NetworkConst.Params.DEVICE_ID) deviceId: String
    ): AuthResponse<StudentEntity>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Auth.SIGN_UP_TEACHER)
    suspend fun signUpTeacher(
        @Field(NetworkConst.Params.NAME) name: String,
        @Field(NetworkConst.Params.DESIGNATION) designation: String,
        @Field(NetworkConst.Params.DEPARTMENT) department: String,
        @Field(NetworkConst.Params.EMAIL) email: String,
        @Field(NetworkConst.Params.PASSWORD) password: String,
        @Field(NetworkConst.Params.FACULTY_ID) facultyId: Int,
        @Field(NetworkConst.Params.DEVICE_ID) deviceId: String
    ): AuthResponse<TeacherEntity>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Auth.SIGN_OUT)
    suspend fun signOut(
        @Field(NetworkConst.Params.ID) id: Int,
        @Field(NetworkConst.Params.USER_TYPE) userType: String,
        @Field(NetworkConst.Params.DEVICE_ID) deviceId: String,
    ): AuthResponse<String>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Auth.SIGN_OUT_FROM_ALL_DEVICE)
    suspend fun signOutFromAllDevice(
        @Field(NetworkConst.Params.ID) id: Int,
        @Field(NetworkConst.Params.USER_TYPE) userType: String,
        @Field(NetworkConst.Params.DEVICE_ID) deviceId: String,
    ): AuthResponse<String>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Auth.CHANGE_PASSWORD)
    suspend fun changePassword(
        @Field(NetworkConst.Params.USER_ID) userId: Int,
        @Field(NetworkConst.Params.USER_TYPE) userType: String,
        @Field(NetworkConst.Params.OLD_PASSWORD) oldPassword: String,
        @Field(NetworkConst.Params.NEW_PASSWORD) newPassword: String,
        @Field(NetworkConst.Params.DEVICE_ID) deviceId: String,
    ): AuthResponse<String>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Auth.FORGOT_PASSWORD)
    suspend fun forgotPassword(
        @Field(NetworkConst.Params.USER_TYPE) userType: String,
        @Field(NetworkConst.Params.EMAIL) email: String,
        @Field(NetworkConst.Params.DEVICE_ID) deviceId: String,
    ): AuthResponse<String>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Auth.EMAIL_VERIFICATION)
    suspend fun emailVerification(
        @Field(NetworkConst.Params.USER_TYPE) userType: String,
        @Field(NetworkConst.Params.EMAIL) email: String,
        @Field(NetworkConst.Params.DEVICE_ID) deviceId: String,
    ): AuthResponse<String>

    @FormUrlEncoded
    @POST(NetworkConst.Remote.Api.Auth.DELETE_ACCOUNT)
    suspend fun deleteAccount(
        @Field(NetworkConst.Params.USER_ID) userId: Int,
        @Field(NetworkConst.Params.USER_TYPE) userType: String,
        @Field(NetworkConst.Params.EMAIL) email: String,
        @Field(NetworkConst.Params.PASSWORD) password: String,
    ): AuthResponse<String>
}