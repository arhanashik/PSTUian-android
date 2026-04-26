package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.model.ApiAuthResponse
import com.workfort.pstuian.data.model.ApiResponse
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.model.TeacherDto
import com.workfort.pstuian.data.remote.NetworkConst
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters

class AuthApiService(private val client: HttpClient) {

    suspend fun signInStudent(
        email: String,
        password: String,
        deviceId: String,
    ): ApiAuthResponse<StudentDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.SIGN_IN,
            formParameters = parameters {
                append(NetworkConst.Params.USER_TYPE, NetworkConst.Params.UserType.STUDENT)
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.PASSWORD, password)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
            }
        ).body()
    }

    suspend fun signInTeacher(
        email: String,
        password: String,
        deviceId: String,
    ): ApiAuthResponse<TeacherDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.SIGN_IN,
            formParameters = parameters {
                append(NetworkConst.Params.USER_TYPE, NetworkConst.Params.UserType.TEACHER)
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.PASSWORD, password)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
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
        password: String,
        deviceId: String,
    ): ApiResponse<StudentDto> {
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
                append(NetworkConst.Params.PASSWORD, password)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
            }
        ).body()
    }

    suspend fun signUpTeacher(
        name: String,
        facultyId: Int,
        designation: String,
        department: String,
        email: String,
        password: String,
        deviceId: String,
    ): ApiResponse<TeacherDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.SIGN_UP_TEACHER,
            formParameters = parameters {
                append(NetworkConst.Params.NAME, name)
                append(NetworkConst.Params.FACULTY_ID, facultyId.toString())
                append(NetworkConst.Params.DESIGNATION, designation)
                append(NetworkConst.Params.DEPARTMENT, department)
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.PASSWORD, password)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
            }
        ).body()
    }

    suspend fun updateUserId(
        userId: String,
        userType: String,
        email: String,
        password: String,
    ): ApiResponse<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.UPDATE_USER_ID,
            formParameters = parameters {
                append(NetworkConst.Params.USER_ID, userId)
                append(NetworkConst.Params.USER_TYPE, userType)
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.PASSWORD, password)
            }
        ).body()
    }

    suspend fun signOut(
        userId: String,
        userType: String,
        deviceId: String,
    ): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.SIGN_OUT,
            formParameters = parameters {
                append(NetworkConst.Params.USER_ID, userId)
                append(NetworkConst.Params.USER_TYPE, userType)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
            }
        ).body()
    }

    suspend fun signOutFromAllDevice(
        userId: String,
        userType: String,
        deviceId: String,
    ): ApiResponse<Unit> {
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
        userType: String,
        email: String,
        oldPassword: String,
        newPassword: String,
        deviceId: String,
    ): ApiResponse<String> { // returns new auth token
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.CHANGE_PASSWORD,
            formParameters = parameters {
                append(NetworkConst.Params.USER_TYPE, userType)
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.OLD_PASSWORD, oldPassword)
                append(NetworkConst.Params.NEW_PASSWORD, newPassword)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
            }
        ).body()
    }

    suspend fun deleteAccount(
        email: String,
        userType: String,
        password: String,
    ): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.DELETE_ACCOUNT,
            formParameters = parameters {
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.PASSWORD, password)
                append(NetworkConst.Params.USER_TYPE, userType)
            }
        ).body()
    }
}
