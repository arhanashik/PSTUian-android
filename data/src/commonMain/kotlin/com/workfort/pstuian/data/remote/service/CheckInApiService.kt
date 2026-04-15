package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.NetworkConst
import com.workfort.pstuian.data.dto.CheckInDto
import com.workfort.pstuian.featuredomain.model.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parameters

class CheckInApiService(private val client: HttpClient) {
    suspend fun getAllByLocation(
        locationId: Int,
        page: Int = 1,
        limit: Int = 20,
    ): Response<List<CheckInDto>> {
        return client.get(NetworkConst.Remote.Api.CheckIn.GET_ALL) {
            parameter(NetworkConst.Params.LOCATION_ID, locationId)
            parameter(NetworkConst.Params.PAGE, page)
            parameter(NetworkConst.Params.LIMIT, limit)
        }.body()
    }

    suspend fun getAllByUser(
        userId: Int,
        userType: String,
        page: Int = 1,
        limit: Int = 20,
    ): Response<List<CheckInDto>> {
        return client.get(NetworkConst.Remote.Api.CheckIn.GET_ALL) {
            parameter(NetworkConst.Params.USER_ID, userId)
            parameter(NetworkConst.Params.USER_TYPE, userType)
            parameter(NetworkConst.Params.PAGE, page)
            parameter(NetworkConst.Params.LIMIT, limit)
        }.body()
    }

    suspend fun get(
        userId: Int,
        userType: String
    ): Response<CheckInDto> {
        return client.get(NetworkConst.Remote.Api.CheckIn.GET) {
            parameter(NetworkConst.Params.USER_ID, userId)
            parameter(NetworkConst.Params.USER_TYPE, userType)
        }.body()
    }

    suspend fun checkIn(
        locationId: Int,
        userId: Int,
        userType: String
    ): Response<CheckInDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.CheckIn.CHECK_IN,
            formParameters = parameters {
                append(NetworkConst.Params.LOCATION_ID, locationId.toString())
                append(NetworkConst.Params.USER_ID, userId.toString())
                append(NetworkConst.Params.USER_TYPE, userType)
            }
        ).body()
    }

    suspend fun updatePrivacy(
        id: Int,
        privacy: String,
    ): Response<CheckInDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.CheckIn.PRIVACY,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id.toString())
                append(NetworkConst.Params.PRIVACY, privacy)
            }
        ).body()
    }

    suspend fun delete(id: Int): Response<Int> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.CheckIn.DELETE,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id.toString())
            }
        ).body()
    }
}
