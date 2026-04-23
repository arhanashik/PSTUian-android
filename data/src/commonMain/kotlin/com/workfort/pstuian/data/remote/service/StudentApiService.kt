package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.model.ApiResponse
import com.workfort.pstuian.data.model.StudentDto
import com.workfort.pstuian.data.remote.NetworkConst
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parameters

class StudentApiService(private val client: HttpClient) {

    suspend fun get(userId: String): ApiResponse<StudentDto> {
        return client.get(NetworkConst.Remote.Api.Student.GET) {
            parameter(NetworkConst.Params.USER_ID, userId)
        }.body()
    }

    suspend fun getByEmail(email: String): ApiResponse<StudentDto> {
        return client.get(NetworkConst.Remote.Api.Student.GET_BY_EMAIL) {
            parameter(NetworkConst.Params.EMAIL, email)
        }.body()
    }

    suspend fun changeProfileImage(userId: String, imageUrl: String): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Student.CHANGE_PROFILE_IMAGE,
            formParameters = parameters {
                append(NetworkConst.Params.ID, userId)
                append(NetworkConst.Params.IMAGE_URL, imageUrl)
            }
        ).body()
    }

    suspend fun changeName(userId: String, name: String): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Student.UPDATE_NAME,
            formParameters = parameters {
                append(NetworkConst.Params.ID, userId)
                append(NetworkConst.Params.NAME, name)
            }
        ).body()
    }

    suspend fun changeBio(userId: String, bio: String): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Student.UPDATE_BIO,
            formParameters = parameters {
                append(NetworkConst.Params.ID, userId)
                append(NetworkConst.Params.BIO, bio)
            }
        ).body()
    }

    suspend fun changeAcademicInfo(
        userId: String,
        name: String,
        studentId: String,
        reg: String,
        blood: String,
        facultyId: Int,
        session: String,
        batchId: Int,
    ): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Student.UPDATE_ACADEMIC_INFO,
            formParameters = parameters {
                append(NetworkConst.Params.NAME, name)
                append(NetworkConst.Params.USER_ID, userId)
                append(NetworkConst.Params.ID, studentId)
                append(NetworkConst.Params.REG, reg)
                append(NetworkConst.Params.BLOOD, blood)
                append(NetworkConst.Params.FACULTY_ID, facultyId.toString())
                append(NetworkConst.Params.SESSION, session)
                append(NetworkConst.Params.BATCH_ID, batchId.toString())
            }
        ).body()
    }

    suspend fun changeConnectInfo(
        userId: String,
        address: String,
        phone: String,
        oldEmail: String,
        newEmail: String,
        cvLink: String,
        linkedIn: String,
        fbLink: String,
    ): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Student.UPDATE_CONNECT_INFO,
            formParameters = parameters {
                append(NetworkConst.Params.ID, userId)
                append(NetworkConst.Params.ADDRESS, address)
                append(NetworkConst.Params.PHONE, phone)
                append(NetworkConst.Params.OLD_EMAIL, oldEmail)
                append(NetworkConst.Params.NEW_EMAIL, newEmail)
                append(NetworkConst.Params.CV_LINK, cvLink)
                append(NetworkConst.Params.LINKED_IN, linkedIn)
                append(NetworkConst.Params.FB_LINK, fbLink)
            }
        ).body()
    }
}
