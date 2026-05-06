package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.model.ApiResponse
import com.workfort.pstuian.data.model.DonationDto
import com.workfort.pstuian.data.remote.NetworkConst
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parameters

class DonationApiService(private val client: HttpClient) {

    suspend fun saveDonation(
        name: String,
        email: String,
        reference: String,
        amount: String,
        message: String,
    ): ApiResponse<Unit> {
        return client.submitForm(
            url = "donation.php?call=save",
            formParameters = parameters {
                append("name", name)
                append("email", email)
                append("reference", reference)
                append("amount", amount)
                append("message", message)
            }
        ).body()
    }

    suspend fun getDonors(
        page: Int,
        limit: Int,
    ): ApiResponse<List<DonationDto>> {
        return client.get("donation.php?call=donors") {
            parameter(NetworkConst.Params.PAGE, page)
            parameter(NetworkConst.Params.LIMIT, limit)
        }.body()
    }
}
