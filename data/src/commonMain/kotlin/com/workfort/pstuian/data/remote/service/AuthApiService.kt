package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.model.ApiResponse
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.model.TeacherDto
import com.workfort.pstuian.data.remote.NetworkConst
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters

class AuthApiService(private val client: HttpClient) {

    suspend fun validateStudentSignIn(
        deviceId: String,
    ): ApiResponse<StudentDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.VALIDATE_SIGN_IN,
            formParameters = parameters {
                append(NetworkConst.Params.USER_TYPE, NetworkConst.Params.UserType.STUDENT)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
            }
        ).body()
    }

    suspend fun validateTeacherSignIn(
        deviceId: String,
    ): ApiResponse<TeacherDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.VALIDATE_SIGN_IN,
            formParameters = parameters {
                append(NetworkConst.Params.USER_TYPE, NetworkConst.Params.UserType.TEACHER)
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
                append(NetworkConst.Params.DEVICE_ID, deviceId)
            }
        ).body()
    }

    suspend fun signUpTeacher(
        name: String,
        facultyId: Int,
        designation: String,
        department: String,
        deviceId: String,
    ): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.SIGN_UP_TEACHER,
            formParameters = parameters {
                append(NetworkConst.Params.NAME, name)
                append(NetworkConst.Params.FACULTY_ID, facultyId.toString())
                append(NetworkConst.Params.DESIGNATION, designation)
                append(NetworkConst.Params.DEPARTMENT, department)
                append(NetworkConst.Params.DEVICE_ID, deviceId)
            }
        ).body()
    }

    suspend fun activateAccount(userType: String): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.ACTIVATE_ACCOUNT,
            formParameters = parameters {
                append(NetworkConst.Params.USER_TYPE, userType)
            }
        ).body()
    }

    suspend fun deactivateAccount(userType: String): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.DEACTIVATE_ACCOUNT,
            formParameters = parameters {
                append(NetworkConst.Params.USER_TYPE, userType)
            }
        ).body()
    }

    suspend fun signOut(userType: String, clearAllSession: Boolean): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Auth.SIGN_OUT,
            formParameters = parameters {
                append(NetworkConst.Params.USER_TYPE, userType)
                append(NetworkConst.Params.CLEAR_ALL_SESSION, clearAllSession.toString())
            }
        ).body()
    }
}
