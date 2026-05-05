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

    suspend fun get(studentId: Int): ApiResponse<StudentDto> {
        return client.get(NetworkConst.Remote.Api.Student.GET) {
            parameter(NetworkConst.Params.ID, studentId)
        }.body()
    }

    suspend fun getByEmail(email: String): ApiResponse<StudentDto> {
        return client.get(NetworkConst.Remote.Api.Student.GET_BY_EMAIL) {
            parameter(NetworkConst.Params.EMAIL, email)
        }.body()
    }

    suspend fun changeProfileImage(imageUrl: String): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Student.CHANGE_PROFILE_IMAGE,
            formParameters = parameters {
                append(NetworkConst.Params.IMAGE_URL, imageUrl)
            }
        ).body()
    }

    suspend fun changeName(authUserId: String, name: String): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Student.UPDATE_NAME,
            formParameters = parameters {
                append(NetworkConst.Params.AUTH_USER_ID, authUserId)
                append(NetworkConst.Params.NAME, name)
            }
        ).body()
    }

    suspend fun changeBio(authUserId: String, bio: String): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Student.UPDATE_BIO,
            formParameters = parameters {
                append(NetworkConst.Params.AUTH_USER_ID, authUserId)
                append(NetworkConst.Params.BIO, bio)
            }
        ).body()
    }

    suspend fun changeCvUrl(fileUrl: String): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Student.CHANGE_CV_URL,
            formParameters = parameters {
                append(NetworkConst.Params.CV_LINK, fileUrl)
            }
        ).body()
    }

    suspend fun changeAcademicInfo(
        authUserId: String,
        name: String,
        studentOldId: Int,
        studentId: Int,
        reg: String,
        blood: String,
        facultyId: Int,
        session: String,
        batchId: Int,
    ): ApiResponse<StudentDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Student.UPDATE_ACADEMIC_INFO,
            formParameters = parameters {
                append(NetworkConst.Params.AUTH_USER_ID, authUserId)
                append(NetworkConst.Params.NAME, name)
                append(NetworkConst.Params.OLD_ID, studentOldId.toString())
                append(NetworkConst.Params.ID, studentId.toString())
                append(NetworkConst.Params.REG, reg)
                append(NetworkConst.Params.BLOOD, blood)
                append(NetworkConst.Params.FACULTY_ID, facultyId.toString())
                append(NetworkConst.Params.SESSION, session)
                append(NetworkConst.Params.BATCH_ID, batchId.toString())
            }
        ).body()
    }

    suspend fun changeConnectInfo(
        authUserId: String,
        address: String,
        phone: String,
        oldEmail: String,
        newEmail: String,
        cvLink: String,
        linkedIn: String,
        fbLink: String,
    ): ApiResponse<StudentDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Student.UPDATE_CONNECT_INFO,
            formParameters = parameters {
                append(NetworkConst.Params.AUTH_USER_ID, authUserId)
                append(NetworkConst.Params.ADDRESS, address)
                append(NetworkConst.Params.PHONE, phone)
                append(NetworkConst.Params.OLD_EMAIL, oldEmail)
                append(NetworkConst.Params.EMAIL, newEmail)
                append(NetworkConst.Params.CV_LINK, cvLink)
                append(NetworkConst.Params.LINKED_IN, linkedIn)
                append(NetworkConst.Params.FB_LINK, fbLink)
            }
        ).body()
    }
}
