package com.workfort.pstuian.data.remote.service

import com.workfort.pstuian.data.model.ApiResponse
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
    ): ApiResponse<String> {
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
