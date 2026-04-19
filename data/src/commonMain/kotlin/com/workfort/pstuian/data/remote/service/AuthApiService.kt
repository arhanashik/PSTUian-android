package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.model.ApiResponse
import com.workfort.pstuian.data.model.ConfigDto
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.model.TeacherDto
import com.workfort.pstuian.data.remote.NetworkConst
import com.workfort.pstuian.featuredomain.model.AuthResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters

class AuthApiService(private val client: HttpClient) {
    suspend fun getConfig(): ApiResponse<ConfigDto> {
        return client.get(NetworkConst.Remote.Api.GET_CONFIG).body()
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
        userId: String,
        userType: String,
        deviceId: String,
    ): AuthResponse<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.SIGN_OUT,
            formParameters = parameters {
                append(NetworkConst.Params.ID, userId)
                append(NetworkConst.Params.USER_TYPE, userType)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
            }
        ).body()
    }

    suspend fun signOutFromAllDevice(
        userId: String,
        userType: String,
        deviceId: String,
    ): AuthResponse<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.SIGN_OUT_FROM_ALL_DEVICE,
            formParameters = parameters {
                append(NetworkConst.Params.ID, userId)
                append(NetworkConst.Params.USER_TYPE, userType)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
            }
        ).body()
    }

    suspend fun changePassword(
        userId: String,
        userType: String,
        oldPassword: String,
        newPassword: String,
        deviceId: String,
    ): AuthResponse<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.CHANGE_PASSWORD,
            formParameters = parameters {
                append(NetworkConst.Params.USER_ID, userId)
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
        userId: String,
        userType: String,
        email: String,
        password: String,
    ): AuthResponse<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.DELETE_ACCOUNT,
            formParameters = parameters {
                append(NetworkConst.Params.USER_ID, userId)
                append(NetworkConst.Params.USER_TYPE, userType)
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.PASSWORD, password)
            }
        ).body()
    }
}
