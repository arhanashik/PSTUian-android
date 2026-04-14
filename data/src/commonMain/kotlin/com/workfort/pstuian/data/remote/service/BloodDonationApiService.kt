package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.appconstant.NetworkConst
import com.workfort.pstuian.data.dto.BloodDonationDto
import com.workfort.pstuian.featuredomain.model.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parameters

class BloodDonationApiService(private val client: HttpClient) {
    suspend fun getAll(
        userId: Int,
        userType: String,
        page: Int = 1,
        limit: Int = 20,
    ): Response<List<BloodDonationDto>> {
        return client.get(NetworkConst.Remote.Api.BloodDonation.GET_ALL) {
            parameter(NetworkConst.Params.USER_ID, userId)
            parameter(NetworkConst.Params.USER_TYPE, userType)
            parameter(NetworkConst.Params.PAGE, page)
            parameter(NetworkConst.Params.LIMIT, limit)
        }.body()
    }

    suspend fun get(id: Int): Response<BloodDonationDto> {
        return client.get(NetworkConst.Remote.Api.BloodDonation.GET) {
            parameter(NetworkConst.Params.ID, id)
        }.body()
    }

    suspend fun insert(
        userId: Int,
        userType: String,
        requestId: Int?,
        date: Long,
        info: String?,
    ): Response<BloodDonationDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.BloodDonation.INSERT,
            formParameters = parameters {
                append(NetworkConst.Params.USER_ID, userId.toString())
                append(NetworkConst.Params.USER_TYPE, userType)
                if (requestId != null) append(NetworkConst.Params.REQUEST_ID, requestId.toString())
                append(NetworkConst.Params.DATE, date.toString())
                if (info != null) append(NetworkConst.Params.INFO, info)
            }
        ).body()
    }

    suspend fun update(
        id: Int,
        requestId: Int?,
        date: String,
        info: String?,
    ): Response<BloodDonationDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.BloodDonation.UPDATE,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id.toString())
                if (requestId != null) append(NetworkConst.Params.REQUEST_ID, requestId.toString())
                append(NetworkConst.Params.DATE, date)
                if (info != null) append(NetworkConst.Params.INFO, info)
            }
        ).body()
    }

    suspend fun delete(id: Int): Response<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.BloodDonation.DELETE,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id.toString())
            }
        ).body()
    }
}
