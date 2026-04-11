package com.workfort.pstuian.networking.service

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.model.dto.CheckInLocationDto
import com.workfort.pstuian.model.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parameters

class CheckInLocationApiService(private val client: HttpClient) {
    suspend fun getAll(
        page: Int = 1,
        limit: Int = 20,
    ): Response<List<CheckInLocationDto>> {
        return client.get(NetworkConst.Remote.Api.CheckInLocation.GET_ALL) {
            parameter(NetworkConst.Params.PAGE, page)
            parameter(NetworkConst.Params.LIMIT, limit)
        }.body()
    }

    suspend fun get(id: Int): Response<CheckInLocationDto> {
        return client.get(NetworkConst.Remote.Api.CheckInLocation.GET) {
            parameter(NetworkConst.Params.ID, id)
        }.body()
    }

    suspend fun search(
        query: String,
        page: Int = 1,
        limit: Int = 20,
    ): Response<List<CheckInLocationDto>> {
        return client.get(NetworkConst.Remote.Api.CheckInLocation.SEARCH) {
            parameter(NetworkConst.Params.QUERY, query)
            parameter(NetworkConst.Params.PAGE, page)
            parameter(NetworkConst.Params.LIMIT, limit)
        }.body()
    }

    suspend fun insert(
        userId: Int,
        userType: String,
        name: String,
        details: String? = "",
        imageUrl: String? = "",
        link: String? = "",
    ): Response<CheckInLocationDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.CheckInLocation.INSERT,
            formParameters = parameters {
                append(NetworkConst.Params.USER_ID, userId.toString())
                append(NetworkConst.Params.USER_TYPE, userType)
                append(NetworkConst.Params.NAME, name)
                if (details != null) append(NetworkConst.Params.DETAILS, details)
                if (imageUrl != null) append(NetworkConst.Params.IMAGE_URL, imageUrl)
                if (link != null) append(NetworkConst.Params.LINK, link)
            }
        ).body()
    }
}
