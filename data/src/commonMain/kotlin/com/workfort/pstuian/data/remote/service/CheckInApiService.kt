package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.model.ApiResponse
import com.workfort.pstuian.data.model.CheckInDto
import com.workfort.pstuian.data.remote.NetworkConst
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parameters

class CheckInApiService(private val client: HttpClient) {

    suspend fun getAllByLocation(
        locationId: Int,
        page: Int,
        limit: Int,
    ): ApiResponse<List<CheckInDto>> {
        return client.get(NetworkConst.Remote.Api.CheckIn.GET_ALL) {
            parameter(NetworkConst.Params.LOCATION_ID, locationId)
            parameter(NetworkConst.Params.PAGE, page)
            parameter(NetworkConst.Params.LIMIT, limit)
        }.body()
    }

    suspend fun getAllByUser(
        userId: Int,
        userType: String,
        page: Int,
        limit: Int,
    ): ApiResponse<List<CheckInDto>> {
        return client.get(NetworkConst.Remote.Api.CheckIn.GET_ALL) {
            parameter(NetworkConst.Params.USER_ID, userId)
            parameter(NetworkConst.Params.USER_TYPE, userType)
            parameter(NetworkConst.Params.PAGE, page)
            parameter(NetworkConst.Params.LIMIT, limit)
        }.body()
    }

    suspend fun get(
        userId: Int,
        userType: String,
    ): ApiResponse<CheckInDto> {
        return client.get(NetworkConst.Remote.Api.CheckIn.GET) {
            parameter(NetworkConst.Params.USER_ID, userId)
            parameter(NetworkConst.Params.USER_TYPE, userType)
        }.body()
    }

    suspend fun checkIn(
        locationId: Int,
        userId: Int,
        userType: String,
    ): ApiResponse<CheckInDto> {
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
    ): ApiResponse<CheckInDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.CheckIn.PRIVACY,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id.toString())
                append(NetworkConst.Params.PRIVACY, privacy)
            }
        ).body()
    }

    suspend fun delete(id: Int): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.CheckIn.DELETE,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id.toString())
            }
        ).body()
    }
}
