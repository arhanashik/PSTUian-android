package com.workfort.pstuian.networking.service

import com.workfort.pstuian.model.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters

class SupportApiService(private val client: HttpClient) {
    suspend fun sendInquiry(
        name: String,
        email: String,
        type: String,
        query: String
    ): Response<String> {
        return client.submitForm(
            url = "user_query.php?call=add",
            formParameters = parameters {
                append("name", name)
                append("email", email)
                append("type", type)
                append("query", query)
            }
        ).body()
    }
}
