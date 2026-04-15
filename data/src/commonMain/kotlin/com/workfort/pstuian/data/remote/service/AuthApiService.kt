package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.NetworkConst
import com.workfort.pstuian.data.dto.ConfigDto
import com.workfort.pstuian.data.dto.DeviceDto
import com.workfort.pstuian.data.dto.StudentDto
import com.workfort.pstuian.data.dto.TeacherDto
import com.workfort.pstuian.featuredomain.model.AuthResponse
import com.workfort.pstuian.featuredomain.model.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parameters

class AuthApiService(private val client: HttpClient) {
    suspend fun getConfig(): Response<ConfigDto> {
        return client.get(NetworkConst.Remote.Api.GET_CONFIG).body()
    }

    suspend fun getAllDevices(
        userId: Int,
        userType: String,
        deviceId: String,
        page: Int,
        limit: Int,
    ): Response<List<DeviceDto>> {
        return client.get(NetworkConst.Remote.Api.Device.GET_ALL) {
            parameter(NetworkConst.Params.USER_ID, userId)
            parameter(NetworkConst.Params.USER_TYPE, userType)
            parameter(NetworkConst.Params.DEVICE_ID, deviceId)
            parameter(NetworkConst.Params.PAGE, page)
            parameter(NetworkConst.Params.LIMIT, limit)
        }.body()
    }

    suspend fun registerDevice(
        id: String,
        fcmToken: String,
        model: String,
        androidVersion: String,
        appVersionCode: Int,
        appVersionName: String,
        ipAddress: String,
        lat: String,
        lng: String,
        locale: String
    ): Response<DeviceDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Device.REGISTER,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id)
                append(NetworkConst.Params.FCM_TOKEN, fcmToken)
                append(NetworkConst.Params.MODEL, model)
                append(NetworkConst.Params.ANDROID_VERSION, androidVersion)
                append(NetworkConst.Params.APP_VERSION_CODE, appVersionCode.toString())
                append(NetworkConst.Params.APP_VERSION_NAME, appVersionName)
                append(NetworkConst.Params.IP_ADDRESS, ipAddress)
                append(NetworkConst.Params.LAT, lat)
                append(NetworkConst.Params.LNG, lng)
                append(NetworkConst.Params.LOCALE, locale)
            }
        ).body()
    }

    suspend fun updateFcmToken(
        deviceId: String,
        fcmToken: String
    ): Response<DeviceDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Device.UPDATE_FCM_TOKEN,
            formParameters = parameters {
                append(NetworkConst.Params.DEVICE_ID, deviceId)
                append(NetworkConst.Params.FCM_TOKEN, fcmToken)
            }
        ).body()
    }

    suspend fun signInStudent(
        email: String,
        password: String,
        deviceId: String,
        userType: String = NetworkConst.Params.UserType.STUDENT,
    ): AuthResponse<StudentDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.SIGN_IN,
            formParameters = parameters {
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.PASSWORD, password)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
                append(NetworkConst.Params.USER_TYPE, userType)
            }
        ).body()
    }

    suspend fun signInTeacher(
        email: String,
        password: String,
        deviceId: String,
        userType: String = NetworkConst.Params.UserType.TEACHER,
    ): AuthResponse<TeacherDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.SIGN_IN,
            formParameters = parameters {
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.PASSWORD, password)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
                append(NetworkConst.Params.USER_TYPE, userType)
            }
        ).body()
    }

    suspend fun signUpStudent(
        name: String,
        id: String,
        reg: String,
        facultyId: Int,
        batchId: Int,
        session: String,
        email: String,
        deviceId: String,
        password: String,
    ): AuthResponse<StudentDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.SIGN_UP_STUDENT,
            formParameters = parameters {
                append(NetworkConst.Params.NAME, name)
                append(NetworkConst.Params.ID, id)
                append(NetworkConst.Params.REG, reg)
                append(NetworkConst.Params.FACULTY_ID, facultyId.toString())
                append(NetworkConst.Params.BATCH_ID, batchId.toString())
                append(NetworkConst.Params.SESSION, session)
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
                append(NetworkConst.Params.PASSWORD, password)
            }
        ).body()
    }

    suspend fun signUpTeacher(
        name: String,
        designation: String,
        department: String,
        email: String,
        password: String,
        facultyId: Int,
        deviceId: String
    ): AuthResponse<TeacherDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.SIGN_UP_TEACHER,
            formParameters = parameters {
                append(NetworkConst.Params.NAME, name)
                append(NetworkConst.Params.DESIGNATION, designation)
                append(NetworkConst.Params.DEPARTMENT, department)
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.PASSWORD, password)
                append(NetworkConst.Params.FACULTY_ID, facultyId.toString())
                append(NetworkConst.Params.DEVICE_ID, deviceId)
            }
        ).body()
    }

    suspend fun signOut(
        id: Int,
        userType: String,
        deviceId: String,
    ): AuthResponse<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.SIGN_OUT,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id.toString())
                append(NetworkConst.Params.USER_TYPE, userType)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
            }
        ).body()
    }

    suspend fun signOutFromAllDevice(
        id: Int,
        userType: String,
        deviceId: String,
    ): AuthResponse<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.SIGN_OUT_FROM_ALL_DEVICE,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id.toString())
                append(NetworkConst.Params.USER_TYPE, userType)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
            }
        ).body()
    }

    suspend fun changePassword(
        userId: Int,
        userType: String,
        oldPassword: String,
        newPassword: String,
        deviceId: String,
    ): AuthResponse<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.CHANGE_PASSWORD,
            formParameters = parameters {
                append(NetworkConst.Params.USER_ID, userId.toString())
                append(NetworkConst.Params.USER_TYPE, userType)
                append(NetworkConst.Params.OLD_PASSWORD, oldPassword)
                append(NetworkConst.Params.NEW_PASSWORD, newPassword)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
            }
        ).body()
    }

    suspend fun forgotPassword(
        userType: String,
        email: String,
        deviceId: String,
    ): AuthResponse<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.FORGOT_PASSWORD,
            formParameters = parameters {
                append(NetworkConst.Params.USER_TYPE, userType)
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
            }
        ).body()
    }

    suspend fun emailVerification(
        userType: String,
        email: String,
        deviceId: String,
    ): AuthResponse<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.EMAIL_VERIFICATION,
            formParameters = parameters {
                append(NetworkConst.Params.USER_TYPE, userType)
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
            }
        ).body()
    }

    suspend fun deleteAccount(
        userId: Int,
        userType: String,
        email: String,
        password: String,
    ): AuthResponse<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.DELETE_ACCOUNT,
            formParameters = parameters {
                append(NetworkConst.Params.USER_ID, userId.toString())
                append(NetworkConst.Params.USER_TYPE, userType)
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.PASSWORD, password)
            }
        ).body()
    }
}
