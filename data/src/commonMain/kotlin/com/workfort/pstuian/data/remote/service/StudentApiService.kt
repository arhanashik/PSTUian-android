package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.NetworkConst
import com.workfort.pstuian.data.dto.StudentDto
import com.workfort.pstuian.featuredomain.model.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parameters

class StudentApiService(private val client: HttpClient) {
    suspend fun get(id: Int): Response<StudentDto> {
        return client.get(NetworkConst.Remote.Api.Student.GET) {
            parameter(NetworkConst.Params.ID, id)
        }.body()
    }

    suspend fun changeProfileImage(
        id: Int,
        imageUrl: String,
    ): Response<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Student.CHANGE_PROFILE_IMAGE,
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
            url = NetworkConst.Remote.Api.Student.UPDATE_NAME,
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
            url = NetworkConst.Remote.Api.Student.UPDATE_BIO,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id.toString())
                append(NetworkConst.Params.BIO, bio)
            }
        ).body()
    }

    suspend fun changeAcademicInfo(
        name: String,
        oldId: Int,
        id: Int,
        reg: String,
        blood: String,
        facultyId: Int,
        session: String,
        batchId: Int,
    ): Response<StudentDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Student.UPDATE_ACADEMIC_INFO,
            formParameters = parameters {
                append(NetworkConst.Params.NAME, name)
                append(NetworkConst.Params.OLD_ID, oldId.toString())
                append(NetworkConst.Params.ID, id.toString())
                append(NetworkConst.Params.REG, reg)
                append(NetworkConst.Params.BLOOD, blood)
                append(NetworkConst.Params.FACULTY_ID, facultyId.toString())
                append(NetworkConst.Params.SESSION, session)
                append(NetworkConst.Params.BATCH_ID, batchId.toString())
            }
        ).body()
    }

    suspend fun changeConnectInfo(
        id: Int,
        address: String,
        phone: String,
        email: String,
        oldEmail: String,
        cvLink: String,
        linkedIn: String,
        fbLink: String,
    ): Response<StudentDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.Student.UPDATE_CONNECT_INFO,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id.toString())
                append(NetworkConst.Params.ADDRESS, address)
                append(NetworkConst.Params.PHONE, phone)
                append(NetworkConst.Params.EMAIL, email)
                append(NetworkConst.Params.OLD_EMAIL, oldEmail)
                append(NetworkConst.Params.CV_LINK, cvLink)
                append(NetworkConst.Params.LINKED_IN, linkedIn)
                append(NetworkConst.Params.FB_LINK, fbLink)
            }
        ).body()
    }
}
