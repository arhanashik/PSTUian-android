package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.model.ApiResponse
import com.workfort.pstuian.data.model.CheckInLocationDto
import com.workfort.pstuian.data.remote.NetworkConst
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parameters

class CheckInLocationApiService(private val client: HttpClient) {

    suspend fun getAll(
        page: Int,
        limit: Int,
    ): ApiResponse<List<CheckInLocationDto>> {
        return client.get(NetworkConst.Remote.Api.CheckInLocation.GET_ALL) {
            parameter(NetworkConst.Params.PAGE, page)
            parameter(NetworkConst.Params.LIMIT, limit)
        }.body()
    }

    suspend fun get(id: Int): ApiResponse<CheckInLocationDto> {
        return client.get(NetworkConst.Remote.Api.CheckInLocation.GET) {
            parameter(NetworkConst.Params.ID, id)
        }.body()
    }

    suspend fun search(
        query: String,
        page: Int,
        limit: Int,
    ): ApiResponse<List<CheckInLocationDto>> {
        return client.get(NetworkConst.Remote.Api.CheckInLocation.SEARCH) {
            parameter(NetworkConst.Params.QUERY, query)
            parameter(NetworkConst.Params.PAGE, page)
            parameter(NetworkConst.Params.LIMIT, limit)
        }.body()
    }

    suspend fun insert(
        userType: String,
        name: String,
        details: String?,
        imageUrl: String?,
        link: String?,
    ): ApiResponse<Unit> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.CheckInLocation.INSERT,
            formParameters = parameters {
                append(NetworkConst.Params.USER_TYPE, userType)
                append(NetworkConst.Params.NAME, name)
                append(NetworkConst.Params.DETAILS, details ?: "")
                append(NetworkConst.Params.IMAGE_URL, imageUrl ?: "")
                append(NetworkConst.Params.LINK, link ?: "")
            }
        ).body()
    }
}
