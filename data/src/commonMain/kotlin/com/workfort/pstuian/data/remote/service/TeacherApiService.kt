package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.model.ApiResponse
import com.workfort.pstuian.data.model.TeacherDto
import com.workfort.pstuian.data.remote.NetworkConst
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parameters

class TeacherApiService(private val client: HttpClient) {

    suspend fun get(id: Int): ApiResponse<TeacherDto> {
        return client.get(NetworkConst.Remote.Api.Teacher.GET) {
            parameter(NetworkConst.Params.ID, id)
        }.body()
    }

    suspend fun getByEmail(email: String): ApiResponse<TeacherDto> {
        return client.get(NetworkConst.Remote.Api.Teacher.GET_BY_EMAIL) {
            parameter(NetworkConst.Params.EMAIL, email)
        }.body()
    }

    suspend fun changeProfileImage(
        authUserId: String,
        imageUrl: String,
    ): ApiResponse<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Teacher.CHANGE_PROFILE_IMAGE,
            formParameters = parameters {
                append(NetworkConst.Params.AUTH_USER_ID, authUserId)
                append(NetworkConst.Params.IMAGE_URL, imageUrl)
            }
        ).body()
    }

    suspend fun changeName(
        authUserId: String,
        name: String
    ): ApiResponse<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Teacher.UPDATE_NAME,
            formParameters = parameters {
                append(NetworkConst.Params.AUTH_USER_ID, authUserId)
                append(NetworkConst.Params.NAME, name)
            }
        ).body()
    }

    suspend fun changeBio(
        authUserId: String,
        bio: String
    ): ApiResponse<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Teacher.UPDATE_BIO,
            formParameters = parameters {
                append(NetworkConst.Params.AUTH_USER_ID, authUserId)
                append(NetworkConst.Params.BIO, bio)
            }
        ).body()
    }

    suspend fun changeAcademicInfo(
        authUserId: String,
        name: String,
        designation: String,
        department: String,
        blood: String,
        facultyId: Int,
    ): ApiResponse<TeacherDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Teacher.UPDATE_ACADEMIC_INFO,
            formParameters = parameters {
                append(NetworkConst.Params.AUTH_USER_ID, authUserId)
                append(NetworkConst.Params.NAME, name)
                append(NetworkConst.Params.DESIGNATION, designation)
                append(NetworkConst.Params.DEPARTMENT, department)
                append(NetworkConst.Params.BLOOD, blood)
                append(NetworkConst.Params.FACULTY_ID, facultyId.toString())
            }
        ).body()
    }

    suspend fun changeConnectInfo(
        authUserId: String,
        address: String,
        phone: String,
        oldEmail: String,
        email: String,
        linkedIn: String,
        fbLink: String,
    ): ApiResponse<TeacherDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Teacher.UPDATE_CONNECT_INFO,
            formParameters = parameters {
                append(NetworkConst.Params.AUTH_USER_ID, authUserId)
                append(NetworkConst.Params.ADDRESS, address)
                append(NetworkConst.Params.PHONE, phone)
                append(NetworkConst.Params.OLD_EMAIL, oldEmail)
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.LINKED_IN, linkedIn)
                append(NetworkConst.Params.FB_LINK, fbLink)
            }
        ).body()
    }
}
