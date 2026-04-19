package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.model.ApiResponse
import com.workfort.pstuian.data.model.BloodDonationRequestDto
import com.workfort.pstuian.data.remote.NetworkConst
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parameters

class BloodDonationRequestApiService(private val client: HttpClient) {
    suspend fun getAll(
        page: Int = 1,
        limit: Int = 20,
    ): ApiResponse<List<BloodDonationRequestDto>> {
        return client.get(NetworkConst.Remote.Api.BloodDonationRequest.GET_ALL) {
            parameter(NetworkConst.Params.PAGE, page)
            parameter(NetworkConst.Params.LIMIT, limit)
        }.body()
    }

    suspend fun get(id: Int): ApiResponse<BloodDonationRequestDto> {
        return client.get(NetworkConst.Remote.Api.BloodDonationRequest.GET) {
            parameter(NetworkConst.Params.ID, id)
        }.body()
    }

    suspend fun insert(
        userId: String,
        userType: String,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String?,
    ): ApiResponse<BloodDonationRequestDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.BloodDonationRequest.INSERT,
            formParameters = parameters {
                append(NetworkConst.Params.USER_ID, userId)
                append(NetworkConst.Params.USER_TYPE, userType)
                append(NetworkConst.Params.BLOOD_GROUP, bloodGroup)
                append(NetworkConst.Params.BEFORE_DATE, beforeDate)
                append(NetworkConst.Params.CONTACT, contact)
                if (info != null) append(NetworkConst.Params.INFO, info)
            }
        ).body()
    }

    suspend fun update(
        id: Int,
        bloodGroup: String,
        beforeDate: String,
        contact: String,
        info: String,
    ): ApiResponse<BloodDonationRequestDto> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.BloodDonationRequest.UPDATE,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id.toString())
                append(NetworkConst.Params.BLOOD_GROUP, bloodGroup)
                append(NetworkConst.Params.BEFORE_DATE, beforeDate)
                append(NetworkConst.Params.CONTACT, contact)
                append(NetworkConst.Params.INFO, info)
            }
        ).body()
    }

    suspend fun delete(id: Int): ApiResponse<String> {
        return client.submitForm(
            url = NetworkConst.Remote.Api.BloodDonationRequest.DELETE,
            formParameters = parameters {
                append(NetworkConst.Params.ID, id.toString())
            }
        ).body()
    }
}
