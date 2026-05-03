package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.model.ApiResponse
import com.workfort.pstuian.data.remote.NetworkConst
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters

class AuthApiService(private val client: HttpClient) {

    suspend fun validateSignIn(
        userType: String,
        email: String,
        deviceId: String,
    ): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.VALIDATE_SIGN_IN,
            formParameters = parameters {
                append(NetworkConst.Params.USER_TYPE, userType)
                append(NetworkConst.Params.EMAIL, email)
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
        deviceId: String,
    ): ApiResponse<Unit> {
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
            }
        ).body()
    }

    suspend fun signUpTeacher(
        name: String,
        facultyId: Int,
        designation: String,
        department: String,
        email: String,
        deviceId: String,
    ): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.SIGN_UP_TEACHER,
            formParameters = parameters {
                append(NetworkConst.Params.NAME, name)
                append(NetworkConst.Params.FACULTY_ID, facultyId.toString())
                append(NetworkConst.Params.DESIGNATION, designation)
                append(NetworkConst.Params.DEPARTMENT, department)
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
            }
        ).body()
    }

    suspend fun updateAuthUserId(userType: String): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.UPDATE_AUTH_USER_ID,
            formParameters = parameters {
                append(NetworkConst.Params.USER_TYPE, userType)
            }
        ).body()
    }

    suspend fun deleteAccount(userType: String): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.DELETE_ACCOUNT,
            formParameters = parameters {
                append(NetworkConst.Params.USER_TYPE, userType)
            }
        ).body()
    }
}
