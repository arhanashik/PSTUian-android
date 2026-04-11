package com.workfort.pstuian.networking.service

import com.workfort.pstuian.model.dto.DonorDto
import com.workfort.pstuian.model.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters

class DonationApiService(private val client: HttpClient) {
    suspend fun getDonationOption(): Response<String> {
        return client.get("donation.php?call=option").body()
    }

    suspend fun saveDonation(
        name: String,
        info: String,
        email: String,
        reference: String
    ): Response<Int> {
        return client.submitForm(
            url = "donation.php?call=save",
            formParameters = parameters {
                append("name", name)
                append("info", info)
                append("email", email)
                append("reference", reference)
            }
        ).body()
    }

    suspend fun getDonors(): Response<List<DonorDto>> {
        return client.get("donation.php?call=donors").body()
    }
}
