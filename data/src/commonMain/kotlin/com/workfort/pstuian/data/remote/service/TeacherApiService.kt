package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.data.dto.TeacherDto
import com.workfort.pstuian.featuredomain.model.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parameters

class TeacherApiService(private val client: HttpClient) {
    suspend fun get(id: Int): Response<TeacherDto> {
        return client.get(NetworkConst.Remote.Api.Teacher.GET) {
            parameter(NetworkConst.Params.ID, id)
        }.body()
    }

    suspend fun changeProfileImage(
        id: Int,
        imageUrl: String,
    ): Response<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Teacher.CHANGE_PROFILE_IMAGE,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id.toString())
                append(NetworkConst.Params.IMAGE_URL, imageUrl)
            }
        ).body()
    }

    suspend fun changeName(
        id: Int,
        name: String
    ): Response<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Teacher.UPDATE_NAME,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id.toString())
                append(NetworkConst.Params.NAME, name)
            }
        ).body()
    }

    suspend fun changeBio(
        id: Int,
        bio: String
    ): Response<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Teacher.UPDATE_BIO,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id.toString())
                append(NetworkConst.Params.BIO, bio)
            }
        ).body()
    }

    suspend fun changeAcademicInfo(
        id: Int,
        name: String,
        designation: String,
        reg: String,
        blood: String,
        facultyId: Int,
    ): Response<TeacherDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Teacher.UPDATE_ACADEMIC_INFO,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id.toString())
                append(NetworkConst.Params.NAME, name)
                append(NetworkConst.Params.DESIGNATION, designation)
                append(NetworkConst.Params.DEPARTMENT, reg)
                append(NetworkConst.Params.BLOOD, blood)
                append(NetworkConst.Params.FACULTY_ID, facultyId.toString())
            }
        ).body()
    }

    suspend fun changeConnectInfo(
        id: Int,
        address: String,
        phone: String,
        email: String,
        oldEmail: String,
        linkedIn: String,
        fbLink: String,
    ): Response<TeacherDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Teacher.UPDATE_CONNECT_INFO,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id.toString())
                append(NetworkConst.Params.ADDRESS, address)
                append(NetworkConst.Params.PHONE, phone)
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.OLD_EMAIL, oldEmail)
                append(NetworkConst.Params.LINKED_IN, linkedIn)
                append(NetworkConst.Params.FB_LINK, fbLink)
            }
        ).body()
    }
}
